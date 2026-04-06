package btl.nongnghiep.mqtt;

import btl.nongnghiep.device.dto.CreateDeviceDto;
import btl.nongnghiep.device.service.PendingDeviceService;
import btl.nongnghiep.sensor.dto.SensorDto;
import btl.nongnghiep.sensordata.dto.ReceiveDataDto;
import btl.nongnghiep.sensordata.service.SensorDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final ObjectMapper objectMapper;
    private final PendingDeviceService pendingDeviceService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SensorDataService sensorDataService;

    public MqttService(ObjectMapper objectMapper,
                       PendingDeviceService pendingDeviceService,
                       SimpMessagingTemplate messagingTemplate,
                       SensorDataService sensorDataService) {
        this.objectMapper = objectMapper;
        this.pendingDeviceService = pendingDeviceService;
        this.messagingTemplate = messagingTemplate;
        this.sensorDataService = sensorDataService;
    }

    public void Mqtthandle(String topic, String message){
        System.out.println(topic + ": " + message);
        String[] parts = topic.split("/");

        if (parts.length == 3 && parts[2].equals("add")) {
            String username = parts[1];

            System.out.println("UserId: " + username);

            try {
                CreateDeviceDto deviceDto = objectMapper.readValue(message, CreateDeviceDto.class);

                System.out.println("Device: " + deviceDto.getName());

                if (deviceDto.getSensors() != null) {
                    for (SensorDto sensor : deviceDto.getSensors()) {
                        System.out.println("  Sensor: " + sensor.getTypeSensor());
                    }
                }

                pendingDeviceService.save(username ,deviceDto);
                // khi nao co dang ki dang nhap thi dung cai duoi
                messagingTemplate.convertAndSendToUser(username,"/topic/devices", deviceDto);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }  else if(parts.length == 4 && parts[3].equals("data")) {
            String username = parts[1];
            String idDevice = parts[2];
            System.out.println("Username: " + username);
            System.out.println("IdDevice: " + idDevice);

            try {
                ReceiveDataDto receiveDataDto = objectMapper.readValue(message, ReceiveDataDto.class);
                sensorDataService.handleData(username,receiveDataDto);
                System.out.println("RAW JSON: " + message);
                System.out.println("DTO: " + receiveDataDto.getDatas());
            } catch (Exception e) {
            e.printStackTrace();
            }
        } else {
            System.out.println("Topic không hợp lệ: " + topic);
        }
    }

}