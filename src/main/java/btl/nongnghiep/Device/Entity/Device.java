package btl.nongnghiep.Device.Entity;

import btl.nongnghiep.Actor.Dto.ActorDto;
import btl.nongnghiep.Actor.Entity.Actor;
import btl.nongnghiep.Device.Dto.CreateDeviceDto;
import btl.nongnghiep.Sensor.Dto.SensorDto;
import btl.nongnghiep.Sensor.Entity.Sensor;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {
    @Id
    private String idDevice;
    private String idUser;
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

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
