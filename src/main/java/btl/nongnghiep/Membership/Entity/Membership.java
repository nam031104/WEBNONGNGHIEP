package btl.nongnghiep.Membership.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @Column(name = "id_membership")
    private String idMembership;

    @Column(name = "id_account")
    private String idAccount;

    @Column(name = "rank_name", columnDefinition = "NVARCHAR(50)")
    private String rankName;

    @Column(name = "balance", precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name = "can_control_device")
    private int canControlDevice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public String getIdMembership() { return idMembership; }
    public void setIdMembership(String idMembership) { this.idMembership = idMembership; }

    public String getIdAccount() { return idAccount; }
    public void setIdAccount(String idAccount) { this.idAccount = idAccount; }

    public String getRankName() { return rankName; }
    public void setRankName(String rankName) { this.rankName = rankName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public int getCanControlDevice() { return canControlDevice; }
    public void setCanControlDevice(int canControlDevice) { this.canControlDevice = canControlDevice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}