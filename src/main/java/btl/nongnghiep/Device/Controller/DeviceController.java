package btl.nongnghiep.Device.Controller;

import btl.nongnghiep.Device.Entity.Device;
import btl.nongnghiep.Device.Service.DeviceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @GetMapping("")
    // HttpSession session thay phan RequestParam sau khi co chuc nang dang nhap
    public String deviceManager(Model model, @RequestParam(name = "id") String id){
        //String userId = (String) session.getAttribute("userId");
        List<Device> devices = deviceService.showDevice(id);
        model.addAttribute("devices",devices);
        return "device/index";
    }

    @GetMapping("/edit/{id}")
    public String editDevice(Model model, @PathVariable("id") String id){
        try {
            Device device = deviceService.findDeviceById(id);
            model.addAttribute("device",device);
            return "device/edit";
        } catch (RuntimeException e) {
            model.addAttribute("message", "Không tìm thấy thiết bị");
            return "device/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateDevice(Model model, @PathVariable("id") String id, @ModelAttribute("device") Device device){
        try {
            Device device1 = deviceService.updateDeviceById(id,device.getName(), device.getTypeDevice() );
            return "redirect:/device";
        } catch (RuntimeException e){
            model.addAttribute("message", "Không tìm thấy thiết bị");
            return "device/error";
        }

    }
}
