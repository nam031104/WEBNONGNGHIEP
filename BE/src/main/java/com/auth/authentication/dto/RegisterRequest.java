package com.auth.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username khong duoc de trong")
    @Size(min = 3, max = 100, message = "Username phai tu 3 den 100 ky tu")
    private String username;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    @Size(max = 150, message = "Email khong duoc vuot qua 150 ky tu")
    private String email;

    @NotBlank(message = "Password khong duoc de trong")
    @Size(min = 6, max = 255, message = "Password phai tu 6 ky tu tro len")
    private String password;
}
