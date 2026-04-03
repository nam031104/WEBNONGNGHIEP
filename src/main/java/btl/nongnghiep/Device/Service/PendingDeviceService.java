package btl.nongnghiep.Device.Service;

import btl.nongnghiep.Device.Dto.CreateDeviceDto;
import org.springframework.stereotype.Service;

@Service
public class PendingDeviceService {
    private CreateDeviceDto pendingDevice;

    public void save(CreateDeviceDto device) {
        this.pendingDevice = device;
    }

    public CreateDeviceDto get() {

        return pendingDevice;
    }

    public void clear() {
        this.pendingDevice = null;
    }
}
