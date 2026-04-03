package btl.nongnghiep.Device.Service;

import btl.nongnghiep.Device.Dto.CreateDeviceDto;
import btl.nongnghiep.Device.Entity.Device;
import btl.nongnghiep.Device.Repository.DeviceRepository;
import btl.nongnghiep.Sensor.Dto.SensorDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

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
        device.setIdUser(idUser);
        Device deviceSaved = deviceRepository.save(device);
        return deviceSaved.toDto();
    }

    @Transactional
    public List<CreateDeviceDto> getDevicesByUser(String idUser) {
        List<Device> devices = deviceRepository.findByUserWithActorsAndSensors(idUser);

        return devices.stream()
                .map(device -> device.toDto())
                .toList();
    }




//
//    public List<DeviceDto> showDevice(String id){
//        return deviceRepository.findByUserId(id)
//                .stream()
//                .map(device -> {
//                    DeviceDto deviceDto = new DeviceDto();
//                    deviceDto.loadFromEntity(device);
//                    return deviceDto;
//                })
//                .toList();
//    }
//
//    public DeviceDto findDeviceById(String id) {
//        Optional<Device> optionalDevice = deviceRepository.findById(id);
//        if (optionalDevice.isPresent()) {
//            DeviceDto deviceDto = new DeviceDto();
//            deviceDto.loadFromEntity(optionalDevice.get());
//
//            return deviceDto;
//        } else {
//            throw new RuntimeException("Device not found");
//        }
//    }
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
//    public void deleteDevice(String id) {
//        Optional<Device> optionalDevice = deviceRepository.findById(id);
//        if (optionalDevice.isPresent()) {
//            Device device = optionalDevice.get();
//            deviceRepository.delete(device);
//        } else {
//            throw new RuntimeException("Device not found");
//        }
//    }
}
