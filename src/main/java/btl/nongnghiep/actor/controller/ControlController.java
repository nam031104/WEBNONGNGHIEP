package btl.nongnghiep.actor.controller;

import btl.nongnghiep.actor.dto.ControlCommandDto;
import btl.nongnghiep.actor.dto.DeviceStatusDto;
import btl.nongnghiep.actor.service.ControlService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/control")
@CrossOrigin(origins = "*")
public class ControlController {

    private final ControlService controlService;

    public ControlController(ControlService controlService) {
        this.controlService = controlService;
    }

    @GetMapping("/status/{deviceId}")
    public ResponseEntity<DeviceStatusDto> getStatus(@PathVariable String deviceId, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(controlService.getDeviceStatus(deviceId, username));
    }

    @PostMapping("/command")
    public ResponseEntity<DeviceStatusDto> sendCommand(@RequestBody ControlCommandDto cmd, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(controlService.sendCommand(cmd, username));
    }
}
