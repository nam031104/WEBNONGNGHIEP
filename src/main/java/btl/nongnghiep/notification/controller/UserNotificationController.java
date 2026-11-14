package btl.nongnghiep.notification.controller;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.repository.AccountRepository;
import btl.nongnghiep.account.service.AccountService;
import btl.nongnghiep.notification.dto.NotificationDto;
import btl.nongnghiep.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user/notification")
public class UserNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AccountService accountService;
    // Giao diện chính: http://localhost:8080/notification
    @GetMapping
    public String showPage(Model model, Authentication authentication) {
        String userName = authentication.getName();
        Account account = accountService.findAccountByUserName(userName);

        List<NotificationDto> list = notificationService.getNotificationByUserId(account.getIdAccount());
        model.addAttribute("notifications", list);
        model.addAttribute("unreadCount", list.stream().filter(n -> n.getIsRead() == 0).count());
        return "notification/notification";
    }

    /**
     * CHỨC NĂNG MỚI: Tiếp nhận thông tin từ form để tạo thông báo
     * Nhận accountId và message từ giao diện qua phương thức POST
     */
//    @PostMapping("/api/create")
//    @ResponseBody
//    public ResponseEntity<String> createNotification(@RequestParam String accountId, @RequestParam String message) {
//        try {
//            notificationService.createNotification(accountId, message);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
//        }
//    }

    // Các hàm API bổ trợ
    @PutMapping("/api/{id}/read")
    @ResponseBody
    public ResponseEntity<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/api/read-all/{idAccount}")
    @ResponseBody
    public ResponseEntity<String> markAllAsRead(@PathVariable String idAccount) {
        notificationService.markAllAsRead(idAccount);
        return ResponseEntity.ok("Success");
    }
}
