package btl.nongnghiep.Membership.Repository;

import btl.nongnghiep.Membership.Entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, String> {

    @Query(value = "SELECT m.id_membership, m.id_account, a.username, m.rank_name, " +
            "m.balance, m.can_control_device, m.created_at " +
            "FROM membership m JOIN account a ON m.id_account = a.id_account " +
            "ORDER BY m.created_at DESC", nativeQuery = true)
    List<Object[]> findAllWithUsername();

    Optional<Membership> findByIdAccount(String idAccount);

    @Modifying
    @Query(value = "UPDATE membership SET balance = balance + :amount, " +
            "rank_name = CASE " +
            "  WHEN (balance + :amount) >= 2000000 THEN N'Vàng' " +
            "  WHEN (balance + :amount) >= 500000  THEN N'Bạc' " +
            "  ELSE N'Đồng' END " +
            "WHERE id_account = :idAccount", nativeQuery = true)
    void topUp(@Param("idAccount") String idAccount, @Param("amount") BigDecimal amount);

    // ADMIN: Cập nhật rank và đặt lại tiền theo luật mới
    @Modifying
    @Query(value = "UPDATE membership SET rank_name = :rankName, balance = :balance WHERE id_membership = :id", nativeQuery = true)
    void updateRankWithBalance(@Param("id") String idMembership, @Param("rankName") String rankName, @Param("balance") BigDecimal balance);

    @Modifying
    @Query(value = "UPDATE membership SET can_control_device = :val WHERE id_membership = :id", nativeQuery = true)
    void setCanControlDevice(@Param("id") String idMembership, @Param("val") int val);
}