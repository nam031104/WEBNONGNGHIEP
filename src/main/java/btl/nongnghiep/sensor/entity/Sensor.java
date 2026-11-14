package btl.nongnghiep.sensor.entity;

import btl.nongnghiep.device.entity.Device;
import jakarta.persistence.*;

@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    private String idSensor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device")
    private Device device;
    private String typeSensor;

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getTypeSensor() {
        return typeSensor;
    }

    public void setTypeSensor(String typeSensor) {
        this.typeSensor = typeSensor;
    }
}
