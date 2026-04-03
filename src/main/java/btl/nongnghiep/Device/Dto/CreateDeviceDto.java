package btl.nongnghiep.Device.Dto;

import btl.nongnghiep.Actor.Dto.ActorDto;
import btl.nongnghiep.Actor.Entity.Actor;
import btl.nongnghiep.Device.Entity.Device;
import btl.nongnghiep.Sensor.Dto.SensorDto;
import btl.nongnghiep.Sensor.Entity.Sensor;

import java.util.ArrayList;
import java.util.List;

//Controller nhận DTO > Service chuyển DTO thành model hoặc entity,
//  rồi xử lý > Repository nhận Entity đưa vào DB
//
//Repository lấy từ DB ra Entity > Service xử lý sao đó rồi thành DTO > Controller và trả về DTO
public class CreateDeviceDto {

    private String idDevice;
    private String name;
    private String typeDevice;
    private int status;
    private String note;

    private List<ActorDto> actors = new ArrayList<>();
    private List<SensorDto> sensors = new ArrayList<>();

    public Device toEntity() {

        Device device = new Device();
        device.setIdDevice(this.getIdDevice());
        device.setName(this.getName());
        device.setTypeDevice(this.getTypeDevice());
        device.setNote(this.getNote());

        // Chuyển list ActorDto sang Actor
        if (this.getActors() != null) {
            List<Actor> actorList = this.getActors().stream()
                    .map(actorDto -> {
                        Actor actor = new Actor();
                        actor.setIdActor(actorDto.getIdActor());
                        actor.setTypeActor(actorDto.getTypeActor());
                        actor.setDevice(device); // quan trọng để thiết lập quan hệ
                        return actor;
                    }).toList();
            device.setActors(actorList);
        }

        // Chuyển list SensorDto sang Sensor
        if (this.getSensors() != null) {
            List<Sensor> sensorList = this.getSensors().stream()
                    .map(sensorDto -> {
                        Sensor sensor = new Sensor();
                        sensor.setIdSensor(sensorDto.getIdSensor());
                        sensor.setTypeSensor(sensorDto.getTypeSensor());
                        sensor.setDevice(device); // thiết lập quan hệ
                        return sensor;
                    }).toList();
            device.setSensors(sensorList);
        }

        return device;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

    public List<SensorDto> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDto> sensors) {
        this.sensors = sensors;
    }
}
