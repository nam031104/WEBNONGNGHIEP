package btl.nongnghiep.actor.service;

import btl.nongnghiep.device.entity.Device;
import btl.nongnghiep.device.repository.DeviceRepository;
import btl.nongnghiep.actor.dto.ControlCommandDto;
import btl.nongnghiep.actor.dto.DeviceStatusDto;
import btl.nongnghiep.actor.entity.Actor;
import btl.nongnghiep.actor.repository.ActorRepository;
import btl.nongnghiep.mqtt.MqttService;
import org.springframework.stereotype.Service;

@Service
public class ControlService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ControlService.class);

    private final DeviceRepository deviceRepository;
    private final ActorRepository actorRepository;
    private final MqttService mqttService;

    public ControlService(DeviceRepository deviceRepository, ActorRepository actorRepository, MqttService mqttService) {
        this.deviceRepository = deviceRepository;
        this.actorRepository = actorRepository;
        this.mqttService = mqttService;
    }

    public DeviceStatusDto getDeviceStatus(String deviceId, String username) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Thiết bị không tồn tại: " + deviceId));

        if (!device.getIdAccount().equals(username)) {
            throw new RuntimeException("Bạn không có quyền truy cập thiết bị này!");
        }

        DeviceStatusDto dto = new DeviceStatusDto();
        dto.setIdDevice(device.getIdDevice());
        dto.setName(device.getName());

        // Lấy danh sách các Actor bằng Mapping OneToMany
        java.util.List<Actor> actors = device.getActors();
        // Cần copy để tránh lazy loading issues nếucần, nhưng vì trả về JSON nên Jackson sẽ lo.
        dto.setActors(actors);

        return dto;
    }

    public DeviceStatusDto sendCommand(ControlCommandDto cmd, String username) {
        Device device = deviceRepository.findById(cmd.getIdDevice())
                .orElseThrow(() -> new RuntimeException("Thiết bị không tìm thấy: " + cmd.getIdDevice()));
        
        if (!device.getIdAccount().equals(username)) {
            throw new RuntimeException("Bạn không có quyền điều khiển thiết bị này!");
        }
                
        // Cập nhật cụ thể đúng loại Actor
        Actor actor = actorRepository.findById(cmd.getIdActor())
                .orElseGet(() -> {
                    log.warn("Actor {} không có trong DB. Cố gắng tạo mới.", cmd.getIdActor());
                    Actor a = new Actor();
                    a.setIdActor(cmd.getIdActor());
                    a.setDevice(device);
                    a.setMode(cmd.getMode());
                    a.setStatus(cmd.getStatus());
                    return a;
                });

        actor.setMode(cmd.getMode());
        if (cmd.getStatus() != null) {
            actor.setStatus(cmd.getStatus());
        }
        actorRepository.save(actor);

        // Bắn gói cấu trúc MQTT xuống mạch qua MqttService
        mqttService.publishControlCommand(username, cmd.getIdDevice(), cmd.getIdActor(), cmd.getMode(), cmd.getStatus());
        log.info("Lệnh (Mode: {}, Status: {}) đã được gửi tới Cụm {} của thiết bị {}", cmd.getMode(), cmd.getStatus(), cmd.getIdActor(), cmd.getIdDevice());

        return getDeviceStatus(cmd.getIdDevice(), username);
    }
}
