package btl.nongnghiep.Device.Controller;

import btl.nongnghiep.Device.Dto.CreateDeviceDto;
import btl.nongnghiep.Device.Service.DeviceService;
import btl.nongnghiep.Device.Service.PendingDeviceService;
import btl.nongnghiep.Sensor.Dto.SensorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/device")
public class DeviceController {

    private final PendingDeviceService pendingDeviceService;
    private final DeviceService deviceService;

    public DeviceController(PendingDeviceService pendingDeviceService, DeviceService deviceService) {
        this.pendingDeviceService = pendingDeviceService;
        this.deviceService = deviceService;
    }


    //    @GetMapping("")
//    // HttpSession session thay phan RequestParam sau khi co chuc nang dang nhap
//    public String deviceManager(Model model, @RequestParam(name = "id") String id){
//        //String userId = (String) session.getAttribute("userId");
//        List<DeviceDto> deviceDtos = deviceService.showDevice(id);
//        model.addAttribute("deviceDtos",deviceDtos);
//        return "device/index";
//    }

    @GetMapping("/create")
    public String createDevice(Model model, HttpSession session){
        String idUser = session.getId();
        model.addAttribute("idUser",idUser);
        return "create";
    }

    @PostMapping("/create/confirm")
    public void confirmDevice(HttpSession session) {
        String idUser = session.getId();
        CreateDeviceDto device = pendingDeviceService.get();

        if (device != null) {
            deviceService.createDevice(device,idUser); // lưu DB
            pendingDeviceService.clear(); // xoá pending
        }
    }

//    @Autowired
//    private DeviceService deviceService;
//    @GetMapping("")
//    // HttpSession session thay phan RequestParam sau khi co chuc nang dang nhap
//    public String deviceManager(Model model, @RequestParam(name = "id") String id){
//        //String userId = (String) session.getAttribute("userId");
//        List<DeviceDto> deviceDtos = deviceService.showDevice(id);
//        model.addAttribute("deviceDtos",deviceDtos);
//        return "device/index";
//    }
//
//    @GetMapping("/create")
//    public String createDevice(Model model) {
//        Device device = new Device();
//        // thay bang DTO
//        model.addAttribute("device", device);
//        return "device/create";
//    }
//    @ResponseBody
//    @PostMapping("/create")
//    public String createDevice(Model model, @ModelAttribute("device") Device device){
//
//        return device.getIdDevice() + " " + device.getName();
//    }
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
//    @GetMapping("/delete/{id}")
//    public String deleteDevice(Model model, @PathVariable("id") String id){
//        try{
//            deviceService.deleteDevice(id);
//            return "redirect:/device";
//        } catch (RuntimeException e){
//            model.addAttribute("messeage", "Không tìm thấy thiết bị");
//            return "device/error";
//        }
//    }


}
