package btl.nongnghiep.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data_report")
public class SensorDataReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor_data_report")
    private Long id;

    @Column(name = "id_sensor", nullable = false, length = 255)
    private String idSensor;

    @Column(name = "avg_value")
    private Float avgValue;

    @Column(name = "min_value")
    private Float minValue;

    @Column(name = "max_value")
    private Float maxValue;

    @Column(name = "period_type", length = 10)
    private String periodType;

    @Column(name = "period_time")
    private LocalDateTime periodTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "unit", length = 50)
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public Float getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(Float avgValue) {
        this.avgValue = avgValue;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public LocalDateTime getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(LocalDateTime periodTime) {
        this.periodTime = periodTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
