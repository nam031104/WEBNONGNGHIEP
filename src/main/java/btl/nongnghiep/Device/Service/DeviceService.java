package btl.nongnghiep.Device.Service;

import btl.nongnghiep.Device.Entity.Device;
import btl.nongnghiep.Device.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> showDevice(String id){
        return deviceRepository.findByUserId(id);
    }

    public Device findDeviceById(String id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public Device updateDeviceById(String id, String name, String typeDevice) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);

        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            device.setName(name);
            device.setTypeDevice(typeDevice);
            return deviceRepository.save(device);
        } else {
            throw new RuntimeException("Device not found");
        }
    }
}
