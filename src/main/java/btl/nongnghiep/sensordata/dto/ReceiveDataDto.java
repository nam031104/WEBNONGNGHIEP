package btl.nongnghiep.sensordata.dto;

import btl.nongnghiep.actor.dto.StatusActorDto;
import btl.nongnghiep.actor.entity.Actor;
import btl.nongnghiep.sensor.service.SensorService;
import btl.nongnghiep.sensordata.entity.SensorData;

import java.util.ArrayList;
import java.util.List;

public class ReceiveDataDto {
    private String idDevice;

    private List<StatusActorDto> actors = new ArrayList<>();
    private List<DataDto> sensors = new ArrayList<>();


    public List<SensorData> toDatas(){
        List<SensorData> sensorDatas = new ArrayList<>();
        sensorDatas = this.sensors.stream().map(
                dataDto -> {
                    SensorData data = new SensorData();
                    data.setIdSensor(dataDto.getIdSensor());
                    data.setValue(dataDto.getValue());
                    data.setUnit(dataDto.getUnit());
                    return data;
                }).toList();
        return sensorDatas;
    }

    public List<Actor> toStatusActors(){
        List<Actor> actorList = new ArrayList<>();
        actorList = this.actors.stream().map(
                actorDto -> {
                    Actor actor = new Actor();
                    actor.setIdActor(actorDto.getIdActor());
                    actor.setMode(actorDto.getMode());
                    actor.setStatus(actorDto.getStatus());
                    return actor;
                }).toList();
        return actorList;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public List<StatusActorDto> getActors() {
        return actors;
    }

    public void setActors(List<StatusActorDto> actors) {
        this.actors = actors;
    }

    public List<DataDto> getDatas() {
        return sensors;
    }

    public void setDatas(List<DataDto> datas) {
        this.sensors = datas;
    }

    public List<DataDto> getSensors() {
        return sensors;
    }

    public void setSensors(List<DataDto> sensors) {
        this.sensors = sensors;
    }
}
