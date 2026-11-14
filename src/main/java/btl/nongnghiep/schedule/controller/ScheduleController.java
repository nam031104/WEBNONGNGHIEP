package btl.nongnghiep.schedule.controller;

import btl.nongnghiep.schedule.dto.ScheduleDto;
import btl.nongnghiep.schedule.service.ScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getAllSchedules(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(scheduleService.getSchedulesByUsername(username));
    }

    @GetMapping("/actor/{idActor}")
    public ResponseEntity<List<ScheduleDto>> getByActor(@PathVariable String idActor, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(scheduleService.getSchedulesByActor(idActor, username));
    }

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleDto dto, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(scheduleService.createSchedule(dto, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        scheduleService.deleteSchedule(id, username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable String id, @RequestBody ScheduleDto dto, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto, username));
    }
}
