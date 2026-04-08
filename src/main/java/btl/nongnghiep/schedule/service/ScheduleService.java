package btl.nongnghiep.schedule.service;

import btl.nongnghiep.actor.entity.Actor;
import btl.nongnghiep.schedule.dto.ScheduleDto;
import btl.nongnghiep.schedule.entity.Schedule;
import btl.nongnghiep.actor.repository.ActorRepository;
import btl.nongnghiep.schedule.repository.ScheduleRepository;
import btl.nongnghiep.mqtt.MqttService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;
    private final ActorRepository actorRepository;
    private final MqttService mqttService;

    public ScheduleService(ScheduleRepository scheduleRepository, ActorRepository actorRepository, MqttService mqttService) {
        this.scheduleRepository = scheduleRepository;
        this.actorRepository = actorRepository;
        this.mqttService = mqttService;
    }

    // === CRUD ===
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ScheduleDto> getSchedulesByUsername(String username) {
        return scheduleRepository.findSchedulesByAccountId(username).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ScheduleDto> getSchedulesByActor(String idActor) {
        return scheduleRepository.findByIdActor(idActor).stream().map(this::toDto).collect(Collectors.toList());
    }

    public ScheduleDto createSchedule(ScheduleDto dto) {
        Schedule s = new Schedule();
        s.setIdActor(dto.getIdActor());
        s.setDate(dto.getDate());
        s.setMode(dto.getMode());
        s.setStatus(dto.getStatus()); 
        s.setIsExecuted(0); // 0 = Chưa chạy
        s.setNote(dto.getNote());
        
        return toDto(scheduleRepository.save(s));
    }

    // Xóa
    public void deleteSchedule(String id) {
        scheduleRepository.deleteById(id);
    }

    // Sửa
    public ScheduleDto updateSchedule(String id, ScheduleDto dto) {
        Schedule s = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        s.setIdActor(dto.getIdActor());
        s.setDate(dto.getDate());
        s.setNote(dto.getNote());
        s.setMode(dto.getMode());
        s.setStatus(dto.getStatus());
        
        // Reset biáº¿n Ä‘á»ƒ backend cÃ³ thá»ƒ cháº¡y láº¡i lá»‡nh vÃ o Ä‘Ãºng thá» i Ä‘iá»ƒm má»›i
        s.setIsExecuted(0); 
        
        return toDto(scheduleRepository.save(s));
    }

    /**
     * Tự động quét mỗi đầu phút (giây thứ 0).
     */
    @Scheduled(cron = "0 * * * * *")
    public void executeScheduledJobs() {
        List<Schedule> pendings = scheduleRepository.findPendingSchedules(LocalDateTime.now());
        for (Schedule schedule : pendings) {
            try {
                Optional<Actor> optActor = actorRepository.findById(schedule.getIdActor());
                if (optActor.isPresent()) {
                    Actor actor = optActor.get();
                    
                    // Cập nhật trạng thái
                    actor.setMode(schedule.getMode());
                    actor.setStatus(schedule.getStatus());
                    actorRepository.save(actor);

                    // Bắn lệnh
                    String username = actor.getDevice() != null ? actor.getDevice().getIdAccount() : "system";
                    String deviceId = actor.getDevice() != null ? actor.getDevice().getIdDevice() : "unknown";
                    mqttService.publishControlCommand(username, deviceId, actor.getIdActor(), schedule.getMode(), schedule.getStatus());
                    
                    schedule.setIsExecuted(1);
                    scheduleRepository.save(schedule);
                    log.info("===> [SCHEDULE HOÀN THÌNH] Thực thi xong lệnh cho lịch {} (Actor: {})", schedule.getIdSchedule(), actor.getIdActor());
                } else {
                    schedule.setIsExecuted(1);
                    schedule.setNote("Actor không tồn tại");
                    scheduleRepository.save(schedule);
                    log.warn("CẢNH BÁO: Không tìm thấy Actor '{}' trong DB cho Lịch {}. Bỏ qua lệnh gửi MQTT.", 
                             schedule.getIdActor(), schedule.getIdSchedule());
                }
            } catch (Exception e) {
                log.error("Lỗi khi chạy schedule {}: {}", schedule.getIdSchedule(), e.getMessage());
            }
        }
    }

    private ScheduleDto toDto(Schedule s) {
        return new ScheduleDto(s.getIdSchedule(), s.getIdActor(), s.getDate(), 
                               s.getMode(), s.getStatus(), s.getIsExecuted(), s.getNote());
    }
}
