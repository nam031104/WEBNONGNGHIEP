package btl.nongnghiep.Membership.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MembershipDto {

    private String idMembership;
    private String idAccount;
    private String username;
    private String rankName;
    private BigDecimal balance;
    private int canControlDevice;
    private String createdAtFormatted;

    public MembershipDto(String idMembership, String idAccount, String username,
                         String rankName, BigDecimal balance,
                         int canControlDevice, LocalDateTime createdAt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        this.idMembership = idMembership;
        this.idAccount = idAccount;
        this.username = username;
        this.rankName = rankName;
        this.balance = balance;
        this.canControlDevice = canControlDevice;
        this.createdAtFormatted = (createdAt != null) ? createdAt.format(fmt) : "";
    }

    public String getIdMembership() { return idMembership; }
    public void setIdMembership(String idMembership) { this.idMembership = idMembership; }

    public String getIdAccount() { return idAccount; }
    public void setIdAccount(String idAccount) { this.idAccount = idAccount; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRankName() { return rankName; }
    public void setRankName(String rankName) { this.rankName = rankName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public int getCanControlDevice() { return canControlDevice; }
    public void setCanControlDevice(int canControlDevice) { this.canControlDevice = canControlDevice; }

    public String getCreatedAtFormatted() { return createdAtFormatted; }
    public void setCreatedAtFormatted(String createdAtFormatted) { this.createdAtFormatted = createdAtFormatted; }
}