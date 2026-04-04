package btl.nongnghiep.device.service;

import btl.nongnghiep.device.dto.CreateDeviceDto;
import btl.nongnghiep.device.dto.DeviceDto;
import btl.nongnghiep.device.entity.Device;
import btl.nongnghiep.device.repository.DeviceRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    // Spring tu goi ham va truyen deviceRepository
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
    @Transactional
    public CreateDeviceDto createDevice(CreateDeviceDto createDeviceDto,String idUser) {
        Device device = createDeviceDto.toEntity();
        device.setIdAccount(idUser);
        Device deviceSaved = deviceRepository.save(device);
        return deviceSaved.toDto();
    }

    @Transactional
    public List<DeviceDto> getDevicesByUser(String idUser) {
        List<Device> devices = deviceRepository.findByUserIdWithActors(idUser);
        devices = deviceRepository.findByUserIdWithSensors(idUser);

        return devices.stream()
                .map(device -> device.toDeviceDto())
                .toList();
    }

    public void deleteDevice(String id) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            deviceRepository.delete(device);
        } else {
            throw new RuntimeException("Device not found");
        }
    }




//
//    public DeviceDto updateDeviceById(DeviceDto deviceDto) {
//        String id = deviceDto.getIdDevice();
//        Optional<Device> optionalDevice = deviceRepository.findById(id);
//
//        if (optionalDevice.isPresent()) {
//            Device device = optionalDevice.get();
//            device.setName(deviceDto.getName());
//            device.setTypeDevice(deviceDto.getTypeDevice());
//
//            deviceDto.loadFromEntity(deviceRepository.save(device));
//
//            return deviceDto;
//        } else {
//            throw new RuntimeException("Device not found");
//        }
//    }
//

}
