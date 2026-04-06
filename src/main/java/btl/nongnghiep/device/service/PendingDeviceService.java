package btl.nongnghiep.device.service;

import btl.nongnghiep.device.dto.CreateDeviceDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PendingDeviceService {
    private Map<String, CreateDeviceDto> pendingDevices = new HashMap<>();

    public void save(String username,CreateDeviceDto device) {
        this.pendingDevices.put(username,device);
    }

    public CreateDeviceDto get(String username) {
        return this.pendingDevices.get(username);
    }

    public void clear(String username) {
        this.pendingDevices.remove(username);
    }
}
