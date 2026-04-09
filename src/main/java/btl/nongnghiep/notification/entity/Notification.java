package btl.nongnghiep.notification.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @Column(name = "id_notification")
    private String idNotification;

    @Column(name = "id_account")
    private String idAccount;

    @Column(name = "message", columnDefinition = "NVARCHAR(MAX)")
    private String message;

    @Column(name = "is_read")
    private int isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getter và Setter (Tự sinh hoặc viết tay)
    public String getIdNotification() { return idNotification; }
    public void setIdNotification(String idNotification) { this.idNotification = idNotification; }
    public String getIdAccount() { return idAccount; }
    public void setIdAccount(String idAccount) { this.idAccount = idAccount; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}