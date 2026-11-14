package btl.nongnghiep.sensordata.controller;

import btl.nongnghiep.sensordata.service.SensorDataService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class SensorDataController {
    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService){
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/data")
    public String watchRealtimeData(Authentication authentication){
        return "sensorData/sensorData";
    }
}
