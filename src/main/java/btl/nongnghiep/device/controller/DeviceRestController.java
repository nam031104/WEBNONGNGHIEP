package btl.nongnghiep.device.controller;

import btl.nongnghiep.device.dto.DeviceDto;
import btl.nongnghiep.device.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/api/devices")
public class DeviceRestController {

    private final DeviceService deviceService;

    public DeviceRestController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getDevices(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(deviceService.getDevicesByUsername(username));
    }
}
