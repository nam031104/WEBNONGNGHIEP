package btl.nongnghiep.Mqtt;

import btl.nongnghiep.Device.Controller.DeviceController;
import btl.nongnghiep.Device.Dto.CreateDeviceDto;
import btl.nongnghiep.Device.Service.DeviceService;
import btl.nongnghiep.Device.Service.PendingDeviceService;
import btl.nongnghiep.Sensor.Dto.SensorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final ObjectMapper objectMapper;
    private final PendingDeviceService pendingDeviceService;
    private final SimpMessagingTemplate messagingTemplate;

    public MqttService(ObjectMapper objectMapper,
                       PendingDeviceService pendingDeviceService,
                       SimpMessagingTemplate messagingTemplate) {
        this.objectMapper = objectMapper;
        this.pendingDeviceService = pendingDeviceService;
        this.messagingTemplate = messagingTemplate;
    }

    public void Mqtthandle(String topic, String message){
        System.out.println(topic + ": " + message);
        String[] parts = topic.split("/");

        if (parts.length == 3 && parts[2].equals("add")) {
            String idUser = parts[1];

            System.out.println("UserId: " + idUser);

            try {
                CreateDeviceDto deviceDto = objectMapper.readValue(message, CreateDeviceDto.class);

                System.out.println("Device: " + deviceDto.getName());

                if (deviceDto.getSensors() != null) {
                    for (SensorDto sensor : deviceDto.getSensors()) {
                        System.out.println("  Sensor: " + sensor.getTypeSensor());
                    }
                }

                pendingDeviceService.save(deviceDto);
                messagingTemplate.convertAndSendToUser(idUser,"/topic/devices",deviceDto);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }  else if(parts.length == 4 && parts[4].equals("data")) {
            String idUser = parts[1];
            String idDevice = parts[2];
            System.out.println("UserId: " + idUser);
            System.out.println("IdDevice: " + idDevice);
        } else {
            System.out.println("Topic không hợp lệ: " + topic);
        }
    }

}