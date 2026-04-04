package btl.nongnghiep.device.controller;

import btl.nongnghiep.account.Account;
import btl.nongnghiep.device.dto.CreateDeviceDto;
import btl.nongnghiep.device.dto.DeviceDto;
import btl.nongnghiep.device.service.DeviceService;
import btl.nongnghiep.device.service.PendingDeviceService;
import btl.nongnghiep.mqtt.Mqtt;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/device")
public class DeviceController {

    private final PendingDeviceService pendingDeviceService;
    private final DeviceService deviceService;
    private final Mqtt mqtt;

    public DeviceController(PendingDeviceService pendingDeviceService, DeviceService deviceService, Mqtt mqtt) {
        this.pendingDeviceService = pendingDeviceService;
        this.deviceService = deviceService;
        this.mqtt = mqtt;
    }


    @GetMapping("")
    public String deviceManager(Model model, HttpSession session) {
        //String userId = (String) session.getAttribute("userId");
        Account account = (Account) session.getAttribute("user");
        if (account == null) {
            return "redirect:/device/login";
        }
        String id = account.getIdAccount();
        List<DeviceDto> deviceDtos = deviceService.getDevicesByUser(id);
        model.addAttribute("deviceDtos", deviceDtos);
        return "device/index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        Account user = new Account("1", "nam", "nam");
        String idUser = user.getIdAccount();
        session.setAttribute("user", user);
        //khi dang ky thi sub
        mqtt.MqttSub("esp/" + idUser + "/add", 2);
        System.out.println("da sub");
        return "redirect:/device";
    }

    @GetMapping("/create")
    public String createDevice(Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/device/login";
        }
        String idUser = user.getIdAccount();
        model.addAttribute("idUser", idUser);
        return "device/create";
    }

    @PostMapping("/create/confirm")
    public String confirmDevice(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/device/login";
        }
        String idUser = user.getIdAccount();
        CreateDeviceDto device = pendingDeviceService.get(idUser);

        if (device != null) {
            deviceService.createDevice(device, idUser); // lưu DB
            pendingDeviceService.clear(idUser); // xoá pending
        }
        return "redirect:/device";
    }

    @PostMapping("/create/cancel")
    public String cancelDevice(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/device/login";
        }
        String idUser = user.getIdAccount();
        CreateDeviceDto device = pendingDeviceService.get(idUser);

        if (device != null) {
            deviceService.createDevice(device, idUser); // lưu DB
            pendingDeviceService.clear(idUser); // xoá pending
        }
        return "redirect:/device";
    }

    @GetMapping("/delete/{id}")
    public String deleteDevice(Model model, @PathVariable("id") String id) {
        try {
            deviceService.deleteDevice(id);
            return "redirect:/device";
        } catch (RuntimeException e) {
            model.addAttribute("messeage", "Không tìm thấy thiết bị");
            return "device/error";
        }
    }

//
//    @GetMapping("/edit/{id}")
//    public String editDevice(Model model, @PathVariable("id") String id){
//        try {
//            DeviceDto deviceDto = deviceService.findDeviceById(id);
//            model.addAttribute("devicedto",deviceDto);
//            return "device/edit";
//        } catch (RuntimeException e) {
//            model.addAttribute("message", "Không tìm thấy thiết bị");
//            return "device/error";
//        }
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateDevice(Model model, @PathVariable("id") String id, @ModelAttribute("devicedto") DeviceDto deviceDto){
//        try {
//            deviceDto.setIdDevice(id);
//            deviceService.updateDeviceById(deviceDto);
//            return "redirect:/device";
//        } catch (RuntimeException e){
//            model.addAttribute("message", "Không tìm thấy thiết bị");
//            return "device/error";
//        }
//
//    }
//

//    }
}
