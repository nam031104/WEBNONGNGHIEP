package btl.nongnghiep.authentication.service;

import btl.nongnghiep.authentication.dto.LoginRequest;
import btl.nongnghiep.authentication.dto.LoginResponse;
import btl.nongnghiep.authentication.dto.RegisterRequest;
import btl.nongnghiep.authentication.dto.RegisterResponse;
import btl.nongnghiep.authentication.entity.Account;
import btl.nongnghiep.authentication.exception.InvalidCredentialsException;
import btl.nongnghiep.authentication.exception.ResourceAlreadyExistsException;
import btl.nongnghiep.authentication.repository.AccountRepository;
import btl.nongnghiep.authentication.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    public static final String SESSION_ACCOUNT_ID = "ACCOUNT_ID";
    public static final String SESSION_USERNAME = "USERNAME";
    public static final String SESSION_ROLE = "ROLE";
    public static final String SESSION_LOGIN_AT = "LOGIN_AT";

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

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
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

            createLoginSession(httpRequest, authentication, account, expirationTime);

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
            SecurityContextHolder.clearContext();
            log.error("Authentication failed for account: {}", request.getUsername(), e);
            throw new InvalidCredentialsException("Username hoac password khong chinh xac");
        }
    }

    private void createLoginSession(HttpServletRequest httpRequest,
            Authentication authentication,
            Account account,
            long expirationTime) {
        HttpSession existingSession = httpRequest.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setMaxInactiveInterval((int) TimeUnit.MILLISECONDS.toSeconds(expirationTime));
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        session.setAttribute(SESSION_ACCOUNT_ID, account.getIdAccount());
        session.setAttribute(SESSION_USERNAME, account.getUsername());
        session.setAttribute(SESSION_ROLE, account.getRole());
        session.setAttribute(SESSION_LOGIN_AT, System.currentTimeMillis());
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

