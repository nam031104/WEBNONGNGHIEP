package btl.nongnghiep.schedule.dto;

import java.time.LocalDateTime;

public class ScheduleDto {
    private String idSchedule;
    private String idActor;
    private LocalDateTime date;
    private Integer mode;
    private Integer status;
    private Integer isExecuted;
    private String note;

    public ScheduleDto() {
    }

    public ScheduleDto(String idSchedule, String idActor, LocalDateTime date, Integer mode, Integer status, Integer isExecuted, String note) {
        this.idSchedule = idSchedule;
        this.idActor = idActor;
        this.date = date;
        this.mode = mode;
        this.status = status;
        this.isExecuted = isExecuted;
        this.note = note;
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
}
