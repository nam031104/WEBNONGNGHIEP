package btl.nongnghiep.device.controller;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.repository.AccountRepository;
import btl.nongnghiep.device.dto.CreateDeviceDto;
import btl.nongnghiep.device.dto.DeviceDto;
import btl.nongnghiep.device.dto.UpdateDeviceDto;
import btl.nongnghiep.device.service.DeviceService;
import btl.nongnghiep.device.service.PendingDeviceService;
import btl.nongnghiep.mqtt.Mqtt;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/user/device")
public class DeviceController {

    private final PendingDeviceService pendingDeviceService;
    private final DeviceService deviceService;
    private final Mqtt mqtt;

    public DeviceController(PendingDeviceService pendingDeviceService, DeviceService deviceService,Mqtt mqtt) {
        this.pendingDeviceService = pendingDeviceService;
        this.deviceService = deviceService;
        this.mqtt = mqtt;
    }


    @GetMapping("")
    public String deviceManager(Model model, Authentication authentication) {

        String username = authentication.getName();
        List<DeviceDto> deviceDtos = deviceService.getDevicesByUsername(username);
        model.addAttribute("deviceDtos", deviceDtos);
        return "device/index";
    }


    @GetMapping("/create")
    public String createDevice(Model model, Authentication authentication) {
        String username = authentication.getName();
        mqtt.MqttSub("esp/"+username+"/add",2);
        model.addAttribute("username", username);
        return "device/create";
    }

    @PostMapping("/create/confirm")
    public String confirmDevice(Authentication authentication) {
        String username = authentication.getName();
        CreateDeviceDto device = pendingDeviceService.get(username);

        if (device != null) {
            deviceService.createDevice(device, username); // lưu DB
            pendingDeviceService.clear(username); // xoá pending
        }
        return "redirect:/user/device";
    }

    @PostMapping("/create/cancel")
    public String cancelDevice(Authentication authentication) {
        String username = authentication.getName();

        CreateDeviceDto device = pendingDeviceService.get(username);

        if (device != null) {
            deviceService.createDevice(device, username); // lưu DB
            pendingDeviceService.clear(username); // xoá pending
        }
        return "redirect:/user/device";
    }

    @GetMapping("/delete/{id}")
    public String deleteDevice(Model model, @PathVariable("id") String id) {
        try {
            deviceService.deleteDevice(id);
            return "redirect:/user/device";
        } catch (RuntimeException e) {
            model.addAttribute("messeage", "Không tìm thấy thiết bị");
            return "device/error";
        }
    }


    @GetMapping("/update/{id}")
    public String editDevice(Model model, @PathVariable("id") String id){
        try {
            UpdateDeviceDto updateDeviceDto = deviceService.findDeviceById(id);
            model.addAttribute("updateDeviceDto",updateDeviceDto);
            return "device/edit";
        } catch (RuntimeException e) {
            model.addAttribute("message", "Không tìm thấy thiết bị");
            return "device/error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateDevice(Model model, @PathVariable("id") String id, @ModelAttribute("updateDeviceDto") UpdateDeviceDto updateDeviceDto){
        try {
            updateDeviceDto.setIdDevice(id);
            deviceService.updateDeviceById(updateDeviceDto);
            return "redirect:/user/device";
        } catch (RuntimeException e){
            model.addAttribute("message", "Không tìm thấy thiết bị");
            return "device/error";
        }

    }


//    }
}
