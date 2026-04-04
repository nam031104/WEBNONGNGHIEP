package btl.nongnghiep.device.service;

import btl.nongnghiep.device.dto.CreateDeviceDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PendingDeviceService {
    private Map<String, CreateDeviceDto> pendingDevices = new HashMap<>();

    public void save(String idUser,CreateDeviceDto device) {
        this.pendingDevices.put(idUser,device);
    }

    public CreateDeviceDto get(String idUser) {
        return this.pendingDevices.get(idUser);
    }

    public void clear(String idUser) {
        this.pendingDevices.remove(idUser);
    }
}
