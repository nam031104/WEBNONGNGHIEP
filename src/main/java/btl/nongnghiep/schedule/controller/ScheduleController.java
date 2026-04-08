package btl.nongnghiep.schedule.controller;

import btl.nongnghiep.schedule.dto.ScheduleDto;
import btl.nongnghiep.schedule.service.ScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
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
    public ResponseEntity<List<ScheduleDto>> getByActor(@PathVariable String idActor) {
        // Ideally validate that idActor belongs to user here too
        return ResponseEntity.ok(scheduleService.getSchedulesByActor(idActor));
    }

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable String id, @RequestBody ScheduleDto dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }
}
