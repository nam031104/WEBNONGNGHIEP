package btl.nongnghiep.sensordata.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idData;
    private String idSensor;
    private Float value;
    private String unit;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @jakarta.persistence.PrePersist
//    protected void onCreate() {
//        if (this.createdAt == null) {
//            this.createdAt = java.time.LocalDateTime.now().toString();
//        }
//    }

    public String getIdData() {
        return idData;
    }

    public void setIdData(String idData) {
        this.idData = idData;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
