package btl.nongnghiep.Membership.Controller;

import btl.nongnghiep.Membership.Dto.MembershipDto;
import btl.nongnghiep.Membership.Service.MembershipService;
import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String showUserPage(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Account account = accountService.findAccountByUserName(userName);
        String idAccount = account.getIdAccount();
        MembershipDto dto = membershipService.getMembershipByAccount(idAccount);
        if (dto == null) {
            membershipService.createDefaultMembership(idAccount);
            dto = membershipService.getMembershipByAccount(idAccount);
        }
        model.addAttribute("membership", dto);
        model.addAttribute("idAccount", idAccount);
        return "membership/membership-user";
    }

//    @GetMapping("/admin")
//    public String showAdminPage(Model model) {
//        List<MembershipDto> list = membershipService.getAllMemberships();
//        model.addAttribute("memberships", list);
//        return "membership-admin";
//    }
//
    @PostMapping("/api/topup")
    @ResponseBody
    public ResponseEntity<String> topUp(@RequestParam String idAccount, @RequestParam BigDecimal amount) {
        try {
            membershipService.topUp(idAccount, amount);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
//
//    @PutMapping("/api/{id}/rank")
//    @ResponseBody
//    public ResponseEntity<String> setRank(@PathVariable String id, @RequestParam String rankName) {
//        try {
//            membershipService.setRank(id, rankName);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/api/{id}/control-device")
//    @ResponseBody
//    public ResponseEntity<String> setCanControlDevice(@PathVariable String id, @RequestParam int val) {
//        try {
//            membershipService.setCanControlDevice(id, val);
//            return ResponseEntity.ok("Success");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }
}