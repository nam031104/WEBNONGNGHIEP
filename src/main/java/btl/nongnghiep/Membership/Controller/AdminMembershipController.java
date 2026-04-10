package btl.nongnghiep.Membership.Controller;

import btl.nongnghiep.Membership.Dto.MembershipDto;
import btl.nongnghiep.Membership.Service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/membership")
public class AdminMembershipController {
    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public String showAdminPage(Model model) {
        List<MembershipDto> list = membershipService.getAllMemberships();
        model.addAttribute("memberships", list);
        return "membership/membership-admin";
    }


    @PutMapping("/api/{id}/rank")
    @ResponseBody
    public ResponseEntity<String> setRank(@PathVariable String id, @RequestParam String rankName) {
        try {
            membershipService.setRank(id, rankName);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/api/{id}/control-device")
    @ResponseBody
    public ResponseEntity<String> setCanControlDevice(@PathVariable String id, @RequestParam int val) {
        try {
            membershipService.setCanControlDevice(id, val);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
