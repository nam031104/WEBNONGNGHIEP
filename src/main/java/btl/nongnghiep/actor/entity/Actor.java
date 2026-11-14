package btl.nongnghiep.actor.entity;

import btl.nongnghiep.device.entity.Device;
import jakarta.persistence.*;

@Entity
@Table(name = "actor")
public class Actor {
    @Id
    private String idActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device")
    private Device device;

    private String typeActor;
    private int mode;
    private int status;

    public String getIdActor() {
        return idActor;
    }

    public void setIdActor(String idActor) {
        this.idActor = idActor;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getTypeActor() {
        return typeActor;
    }

    public void setTypeActor(String typeActor) {
        this.typeActor = typeActor;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
