package btl.nongnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "id_account", nullable = false, updatable = false, length = 255)
    private String idAccount;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", length = 255)
    private String role;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "number_phone", length = 255)
    private String numberPhone;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    public void normalizeBeforeSave() {
        if (this.idAccount == null || this.idAccount.isBlank()) {
            this.idAccount = UUID.randomUUID().toString();
        }
        this.role = normalizeRole(this.role);
    }

    public static String normalizeRole(String role) {
        if (role == null) {
            return "USER";
        }

        String normalized = role.trim().toUpperCase();
        return "ADMIN".equals(normalized) ? "ADMIN" : "USER";
    }

    public String getNormalizedRole() {
        return normalizeRole(this.role);
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
