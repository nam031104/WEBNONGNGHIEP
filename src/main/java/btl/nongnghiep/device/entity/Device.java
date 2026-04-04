package btl.nongnghiep.device.entity;

import btl.nongnghiep.actor.dto.ActorDto;
import btl.nongnghiep.actor.entity.Actor;
import btl.nongnghiep.device.dto.CreateDeviceDto;
import btl.nongnghiep.device.dto.DeviceDto;
import btl.nongnghiep.sensor.dto.SensorDto;
import btl.nongnghiep.sensor.entity.Sensor;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {
    @Id
    private String idDevice;
    private String idAccount;
    private String name;
    private String typeDevice;
    private String note;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors = new ArrayList<>();


    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public CreateDeviceDto toDto() {

        CreateDeviceDto dto = new CreateDeviceDto();
        dto.setIdDevice(this.getIdDevice());
        dto.setName(this.getName());
        dto.setTypeDevice(this.getTypeDevice());
        dto.setNote(this.getNote());

        // Chuyển list Actor sang ActorDto
        if (this.getActors() != null) {
            List<ActorDto> actorDtos = this.getActors().stream()
                    .map(actor -> {
                        ActorDto actorDto = new ActorDto();
                        actorDto.setIdActor(actor.getIdActor());
                        actorDto.setTypeActor(actor.getTypeActor());
                        return actorDto;
                    }).toList();
            dto.setActors(actorDtos);
        }

        // Chuyển list Sensor sang SensorDto
        if (this.getSensors() != null) {
            List<SensorDto> sensorDtos = this.getSensors().stream()
                    .map(sensor -> {
                        SensorDto sensorDto = new SensorDto();
                        sensorDto.setIdSensor(sensor.getIdSensor());
                        sensorDto.setTypeSensor(sensor.getTypeSensor());
                        return sensorDto;
                    }).toList();
            dto.setSensors(sensorDtos);
        }

        return dto;
    }

    public DeviceDto toDeviceDto() {

        DeviceDto dto = new DeviceDto();
        dto.setIdDevice(this.getIdDevice());
        dto.setName(this.getName());
        dto.setTypeDevice(this.getTypeDevice());
        dto.setNote(this.getNote());

        // Chuyển list Actor sang ActorDto
        if (this.getActors() != null) {
            List<ActorDto> actorDtos = this.getActors().stream()
                    .map(actor -> {
                        ActorDto actorDto = new ActorDto();
                        actorDto.setIdActor(actor.getIdActor());
                        actorDto.setTypeActor(actor.getTypeActor());
                        return actorDto;
                    }).toList();
            dto.setActors(actorDtos);
        }

        // Chuyển list Sensor sang SensorDto
        if (this.getSensors() != null) {
            List<SensorDto> sensorDtos = this.getSensors().stream()
                    .map(sensor -> {
                        SensorDto sensorDto = new SensorDto();
                        sensorDto.setIdSensor(sensor.getIdSensor());
                        sensorDto.setTypeSensor(sensor.getTypeSensor());
                        return sensorDto;
                    }).toList();
            dto.setSensors(sensorDtos);
        }

        return dto;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeDevice() {
        return typeDevice;
    }

    public void setTypeDevice(String typeDevice) {
        this.typeDevice = typeDevice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
