package btl.nongnghiep.notification.repository;

import btl.nongnghiep.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = "SELECT n.id_notification, n.message, n.created_at, n.is_read, a.id_account " +
            "FROM notification n JOIN account a ON n.id_account = a.id_account " +
            "ORDER BY n.created_at DESC", nativeQuery = true)
    List<Object[]> findAllWithUsername();

    @Query(value = "SELECT n.id_notification, n.message, n.created_at, n.is_read, n.id_account " +
            "FROM notification n WHERE n.id_account = :id_account", nativeQuery = true)
    List<Object[]> findAllWithIdAccount(@Param("id_account") String id_account);

    @Modifying
    @Query(value = "UPDATE notification SET is_read = 1 WHERE id_notification = :id", nativeQuery = true)
    void markAsRead(@Param("id") String idNotification);

    @Modifying
    @Query(value = "UPDATE notification SET is_read = 1 WHERE id_account = :idAccount", nativeQuery = true)
    void markAllAsRead(@Param("idAccount") String idAccount);


}