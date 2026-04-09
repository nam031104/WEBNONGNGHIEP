package btl.nongnghiep.actor.dto;

import java.util.List;
public class DeviceStatusDto {
    private String idDevice;
    private String name;
    // private String mode;
    
    // Lưu các đối tượng relay con (Dùng DTO để tránh vòng lặp JSON)
    private List<ActorDto> actors;

    public DeviceStatusDto() {
    }

    public DeviceStatusDto(String idDevice, String name, List<ActorDto> actors) {
        this.idDevice = idDevice;
        this.name = name;
        this.actors = actors;
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

    public List<ActorDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorDto> actors) {
        this.actors = actors;
    }

}
