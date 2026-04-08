package btl.nongnghiep.actor.dto;

import btl.nongnghiep.actor.entity.Actor;
public class DeviceStatusDto {
    private String idDevice;
    private String name;
    // Bá» mode cá»§a device
    // private String mode;
    
    // LÆ°u cÃ¡c Ä‘á»‘i tÆ°á»£ng relay con
    private java.util.List<Actor> actors;

    public DeviceStatusDto() {
    }

    public DeviceStatusDto(String idDevice, String name, java.util.List<Actor> actors) {
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

    public java.util.List<Actor> getActors() {
        return actors;
    }

    public void setActors(java.util.List<Actor> actors) {
        this.actors = actors;
    }

}
