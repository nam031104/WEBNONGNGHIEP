package com.auth.authentication.service;

import com.auth.authentication.dto.LoginRequest;
import com.auth.authentication.dto.LoginResponse;
import com.auth.authentication.dto.RegisterRequest;
import com.auth.authentication.dto.RegisterResponse;
import com.auth.authentication.entity.Account;
import com.auth.authentication.exception.InvalidCredentialsException;
import com.auth.authentication.exception.ResourceAlreadyExistsException;
import com.auth.authentication.repository.AccountRepository;
import com.auth.authentication.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering new account with username: {}", request.getUsername());

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username da ton tai: " + request.getUsername());
        }

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email da ton tai: " + request.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .idAccount(UUID.randomUUID().toString())
                .username(request.getUsername())
                .password(hashedPassword)
                .email(request.getEmail())
                .role("USER")
                .build();

        Account savedAccount = accountRepository.save(account);

        return RegisterResponse.builder()
                .accountId(savedAccount.getIdAccount())
                .username(savedAccount.getUsername())
                .email(savedAccount.getEmail())
                .role(savedAccount.getRole())
                .message("Dang ky tai khoan thanh cong")
                .success(true)
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Account login attempt with username: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            String token = jwtTokenProvider.generateToken(authentication);
            long expirationTime = jwtTokenProvider.getExpirationTime();

            Account account = accountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new InvalidCredentialsException("Username hoac password khong chinh xac"));

            return LoginResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(expirationTime)
                    .accountId(account.getIdAccount())
                    .username(account.getUsername())
                    .email(account.getEmail())
                    .role(account.getRole())
                    .build();
        } catch (Exception e) {
            log.error("Authentication failed for account: {}", request.getUsername(), e);
            throw new InvalidCredentialsException("Username hoac password khong chinh xac");
        }
    }

    @Transactional(readOnly = true)
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Account khong ton tai: " + username));
    }

    public boolean isPasswordCorrect(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
