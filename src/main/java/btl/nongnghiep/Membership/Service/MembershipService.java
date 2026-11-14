package btl.nongnghiep.Membership.Service;

import btl.nongnghiep.Membership.Dto.MembershipDto;
import btl.nongnghiep.Membership.Entity.Membership;
import btl.nongnghiep.Membership.Repository.MembershipRepository;
import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private AccountService accountService;

    public List<MembershipDto> getAllMemberships() {
        List<Object[]> rows = membershipRepository.findAllWithUsername();
        return rows.stream().map(row -> {
            String id = (String) row[0];
            String idAcc = (String) row[1];
            String username = (String) row[2];
            String rankName = (String) row[3];
            BigDecimal bal = (BigDecimal) row[4];

            int canCtrl = 0;
            if (row[5] != null) {
                if (row[5] instanceof Boolean) canCtrl = (Boolean) row[5] ? 1 : 0;
                else if (row[5] instanceof Number) canCtrl = ((Number) row[5]).intValue();
            }

            LocalDateTime dt = (row[6] != null && row[6] instanceof java.sql.Timestamp)
                    ? ((java.sql.Timestamp) row[6]).toLocalDateTime() : (LocalDateTime) row[6];

            return new MembershipDto(id, idAcc, username, rankName, bal, canCtrl, dt);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void setRank(String idMembership, String newRank) {
        Membership m = membershipRepository.findById(idMembership)
                .orElseThrow(() -> new RuntimeException("Thành viên không tồn tại"));

        String oldRank = m.getRankName();
        BigDecimal newBalance = m.getBalance();

        // LOGIC CHỈNH TIỀN KHI ĐỔI HẠNG (PHÚ YÊU CẦU)
        if (newRank.equals("Đồng")) {
            // Từ Bạc hoặc Vàng về Đồng: Xóa hết tiền
            if (oldRank.equals("Bạc") || oldRank.equals("Vàng")) newBalance = BigDecimal.ZERO;
        }
        else if (newRank.equals("Bạc")) {
            // Từ Vàng xuống Bạc HOẶC Đồng lên Bạc: Set về 500k
            if (oldRank.equals("Vàng") || oldRank.equals("Đồng")) newBalance = new BigDecimal("500000");
        }
        else if (newRank.equals("Vàng")) {
            // Từ Đồng hoặc Bạc lên Vàng: Set về 2tr
            if (oldRank.equals("Đồng") || oldRank.equals("Bạc")) newBalance = new BigDecimal("2000000");
        }

        membershipRepository.updateRankWithBalance(idMembership, newRank, newBalance);
    }

    public MembershipDto getMembershipByAccount(String idAccount) {
        return membershipRepository.findByIdAccount(idAccount).map(m -> {
            Account account = accountService.findById(idAccount);
                MembershipDto membershipDto = new MembershipDto(m.getIdMembership(), m.getIdAccount(), account.getUsername(),
                            m.getRankName(), m.getBalance(), m.getCanControlDevice(), m.getCreatedAt());
                    return membershipDto;
                }
        ).orElse(null);
    }

    @Transactional
    public void topUp(String idAccount, BigDecimal amount) {
        membershipRepository.topUp(idAccount, amount);
    }

    @Transactional
    public void setCanControlDevice(String idMembership, int val) {
        membershipRepository.setCanControlDevice(idMembership, val);
    }

    @Transactional
    public void createDefaultMembership(String idAccount) {
        if (membershipRepository.findByIdAccount(idAccount).isPresent()) return;
        Membership m = new Membership();
        m.setIdMembership("MB_" + System.currentTimeMillis());
        m.setIdAccount(idAccount);
        m.setRankName("Đồng");
        m.setBalance(BigDecimal.ZERO);
        m.setCanControlDevice(0);
        m.setCreatedAt(LocalDateTime.now());
        membershipRepository.save(m);
    }
}