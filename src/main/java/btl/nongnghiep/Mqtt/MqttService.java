package btl.nongnghiep.Mqtt;

import org.springframework.stereotype.Service;

@Service
public class MqttService {
    public void Mqtthandle(String topic, String message){
        System.out.println(topic + ": " + message);
    }

}