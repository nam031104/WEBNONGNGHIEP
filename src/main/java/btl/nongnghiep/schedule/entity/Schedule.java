package btl.nongnghiep.schedule.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @Column(name = "id_schedule", length = 255)
    private String idSchedule; // UUID

    @Column(name = "id_actor", length = 255)
    private String idActor; // FK id acotor

    private LocalDateTime date; // thời gian thi hành

    @Column(name = "mode")
    private Integer mode; // cho Actor (1=Auto, 0=Manual)

    @Column(name = "status")
    private Integer status; // cho Actor (1=On, 0=Off)

    @Column(name = "is_executed")
    private Integer isExecuted = 0; //
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Schedule() {
    }

    public Schedule(String idSchedule, String idActor, LocalDateTime date, Integer mode, Integer status,
            Integer isExecuted, String note, LocalDateTime createdAt) {
        this.idSchedule = idSchedule;
        this.idActor = idActor;
        this.date = date;
        this.mode = mode;
        this.status = status;
        this.isExecuted = isExecuted;
        this.note = note;
        this.createdAt = createdAt;
    }

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getIdActor() {
        return idActor;
    }

    public void setIdActor(String idActor) {
        this.idActor = idActor;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public Integer getIsExecuted() {
        return isExecuted;
    }

    public void setIsExecuted(Integer isExecuted) {
        this.isExecuted = isExecuted;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
        if (idSchedule == null)
            idSchedule = java.util.UUID.randomUUID().toString();
    }
}
