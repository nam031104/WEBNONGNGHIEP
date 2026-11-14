package btl.nongnghiep.sensordata.service;

import btl.nongnghiep.actor.entity.Actor;
import btl.nongnghiep.device.dto.DeviceWebSocketDto;
import btl.nongnghiep.sensor.service.SensorService;
import btl.nongnghiep.sensordata.dto.DataWebsocketDto;
import btl.nongnghiep.sensordata.dto.ReceiveDataDto;
import btl.nongnghiep.sensordata.entity.SensorData;
import btl.nongnghiep.sensordata.repository.SensorDataRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataService {
    private final SensorService sensorService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SensorDataRepository sensorDataRepository;

    public SensorDataService(SensorService sensorService,
                             SimpMessagingTemplate messagingTemplate,
                             SensorDataRepository sensorDataRepository){
        this.sensorService = sensorService;
        this.messagingTemplate = messagingTemplate;
        this.sensorDataRepository = sensorDataRepository;
    }

    public void handleData(String username,ReceiveDataDto receiveDataDto){
        List<SensorData> datas = receiveDataDto.toDatas();
        List<Actor> actors = receiveDataDto.toStatusActors();
        if(datas != null){
            DeviceWebSocketDto deviceWebSocketDto = new DeviceWebSocketDto();
            deviceWebSocketDto.setIdDevice(receiveDataDto.getIdDevice());
            List<DataWebsocketDto> dataWebsocketDtos = new ArrayList<>();
            dataWebsocketDtos = datas.stream().map(
                    dataDto -> {
                        DataWebsocketDto data = new DataWebsocketDto();
                        String idSensor = dataDto.getIdSensor();
                        data.setIdSensor(idSensor);
                        data.setTypeSensor(sensorService.getTypeSensorById(idSensor));
                        data.setValue(dataDto.getValue());
                        data.setUnit(dataDto.getUnit());
                        return data;
                    }).toList();
            deviceWebSocketDto.setDataWebsocketDtos(dataWebsocketDtos);
            messagingTemplate.convertAndSendToUser(username,"/topic/sensordata",deviceWebSocketDto );
            sensorDataRepository.saveAll(datas);
        }

    }


}
