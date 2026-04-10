package btl.nongnghiep.actor.dto;

public class ControlCommandDto {
    private String idDevice; // VARCHAR PK
    private String idActor;
    private Integer mode;
    private Integer status;

    public ControlCommandDto() {
    }

    public ControlCommandDto(String idDevice, String idActor, Integer mode, Integer status) {
        this.idDevice = idDevice;
        this.idActor = idActor;
        this.mode = mode;
        this.status = status;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getIdActor() {
        return idActor;
    }

    public void setIdActor(String idActor) {
        this.idActor = idActor;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
