package btl.nongnghiep.device.service;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    // Spring tu goi ham va truyen deviceRepository
    public DeviceService(DeviceRepository deviceRepository,AccountRepository accountRepository) {
        this.deviceRepository = deviceRepository;
        this.accountRepository = accountRepository;
    }
    @Transactional
    public CreateDeviceDto createDevice(CreateDeviceDto createDeviceDto,String username) {
        Device device = createDeviceDto.toEntity();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String idAccount = account.getIdAccount();
        device.setIdAccount(idAccount);
        Device deviceSaved = deviceRepository.save(device);
        return deviceSaved.toDto();
    }

    @Transactional
    public List<DeviceDto> getDevicesByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String idAccount = account.getIdAccount();
        List<Device> devices = deviceRepository.findByUserIdWithActors(idAccount);
        devices = deviceRepository.findByUserIdWithSensors(idAccount);

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
