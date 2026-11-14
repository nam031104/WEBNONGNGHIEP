package btl.nongnghiep.notification.service;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.service.AccountService;
import btl.nongnghiep.notification.dto.NotificationDto;
import btl.nongnghiep.notification.entity.Notification; // THÊM DÒNG NÀY ĐỂ HẾT ĐỎ
import btl.nongnghiep.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AccountService accountService;
    public List<NotificationDto> getAllNotifications() {
        List<Object[]> rows = notificationRepository.findAllWithUsername();
        return rows.stream().map(row -> {
            String id = (String) row[0];
            String msg = (String) row[1];

            LocalDateTime dt = null;
            if (row[2] != null) {
                dt = (row[2] instanceof java.sql.Timestamp) ?
                        ((java.sql.Timestamp) row[2]).toLocalDateTime() : (LocalDateTime) row[2];
            }

            int read = 0;
            if (row[3] != null) {
                read = ((Number) row[3]).intValue();
            }

            String user = (String) row[4];
            return new NotificationDto(id, user, msg, dt, read);
        }).collect(Collectors.toList());
    }

    public List<NotificationDto> getNotificationByUserId(String idAccount){
        List<Object[]> notificationList = notificationRepository.findAllWithIdAccount(idAccount);
        return notificationList.stream().map(row -> {
            String id = (String) row[0];
            String msg = (String) row[1];

            LocalDateTime dt = null;
            if (row[2] != null) {
                dt = (row[2] instanceof java.sql.Timestamp) ?
                        ((java.sql.Timestamp) row[2]).toLocalDateTime() : (LocalDateTime) row[2];
            }

            int read = 0;
            if (row[3] != null) {
                read = ((Number) row[3]).intValue();
            }

            String idUser = (String) row[4];
            Account account = accountService.findById(idUser);
            return new NotificationDto(id, account.getUsername(), msg, dt, read);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(String id) { notificationRepository.markAsRead(id); }

    @Transactional
    public void markAllAsRead(String idAcc) { notificationRepository.markAllAsRead(idAcc); }

    /**
     * CHỨC NĂNG MỚI: Tạo thông báo thủ công
     */
    @Transactional
    public void createNotification(String accountId, String message) {
        // Khởi tạo đối tượng từ Entity
        Notification n = new Notification();

        // Gán dữ liệu (Sử dụng ID dựa trên thời gian thực để không trùng)
        n.setIdNotification("N_" + System.currentTimeMillis());
        n.setIdAccount(accountId);
        n.setMessage(message);
        n.setIsRead(0); // 0 là chưa đọc
        n.setCreatedAt(LocalDateTime.now());

        // Lưu vào Database
        notificationRepository.save(n);
    }
}