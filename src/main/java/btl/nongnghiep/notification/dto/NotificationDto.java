package btl.nongnghiep.notification.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationDto {

    private String idNotification;
    private String content;
    private int isRead;

    public NotificationDto(String idNotification, String username,
                           String message, LocalDateTime createdAt, int isRead) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        this.idNotification = idNotification;
        this.isRead = isRead;
        this.content = "Thông báo đến " + username + ": " + message
                + ", " + createdAt.format(fmt);
    }

    public String getIdNotification() { return idNotification; }
    public void setIdNotification(String idNotification) { this.idNotification = idNotification; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }
}