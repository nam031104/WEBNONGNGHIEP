package btl.nongnghiep.authentication.controller;

import btl.nongnghiep.authentication.dto.LoginRequest;
import btl.nongnghiep.authentication.dto.LoginResponse;
import btl.nongnghiep.authentication.dto.RegisterRequest;
import btl.nongnghiep.authentication.dto.RegisterResponse;
import btl.nongnghiep.authentication.security.JwtTokenProvider;
import btl.nongnghiep.authentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" }, maxAge = 3600)
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("POST /auth/register - username: {}", registerRequest.getUsername());

        try {
            RegisterResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Register failed: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("POST /auth/login - username: {}", loginRequest.getUsername());

        try {
            LoginResponse response = authService.login(loginRequest, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Authentication module is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        response.put("authenticated", authenticated);
        response.put("hasSession", session != null);

        if (session == null || !authenticated) {
            response.put("message", "Chua co phien dang nhap hop le");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Phien dang nhap hop le");
        response.put("username", session.getAttribute(AuthService.SESSION_USERNAME));
        response.put("accountId", session.getAttribute(AuthService.SESSION_ACCOUNT_ID));
        response.put("role", session.getAttribute(AuthService.SESSION_ROLE));
        response.put("loginAt", session.getAttribute(AuthService.SESSION_LOGIN_AT));
        response.put("createdAt", session.getCreationTime());
        response.put("lastAccessedTime", session.getLastAccessedTime());
        response.put("maxInactiveInterval", session.getMaxInactiveInterval());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account-id")
    public ResponseEntity<?> getAuthenticatedAccountId(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountId = resolveAuthenticatedAccountId(authentication, session);

        response.put("authenticated", accountId != null);
        response.put("hasSession", session != null);

        if (accountId == null) {
            response.put("message", "Khong tim thay idAccount trong phien dang nhap");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "Lay idAccount thanh cong");
        response.put("accountId", accountId);
        response.put("username", session != null ? session.getAttribute(AuthService.SESSION_USERNAME) : null);
        response.put("role", session != null ? session.getAttribute(AuthService.SESSION_ROLE) : null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/account-id/verify")
    public ResponseEntity<?> verifyAuthenticatedAccountId(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccountId = resolveAuthenticatedAccountId(authentication, session);
        String requestAccountId = extractAccountId(requestBody);

        if (requestAccountId == null || requestAccountId.isBlank()) {
            response.put("valid", false);
            response.put("message", "Can gui accountId hoac idAccount trong request body");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (currentAccountId == null) {
            response.put("valid", false);
            response.put("message", "Khong co phien dang nhap hop le de doi chieu idAccount");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        boolean valid = currentAccountId.equals(requestAccountId);
        response.put("valid", valid);
        response.put("accountIdFromSession", currentAccountId);
        response.put("accountIdFromRequest", requestAccountId);
        response.put("message", valid
                ? "idAccount trung khop voi phien dang nhap"
                : "idAccount khong trung khop voi phien dang nhap");

        return ResponseEntity.status(valid ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("valid", false);
            response.put("message", "Token khong duoc cung cap hoac dinh dang khong hop le");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String token = authHeader.substring(7);
        boolean valid = jwtTokenProvider.validateToken(token);
        response.put("valid", valid);

        if (!valid) {
            response.put("message", "Token khong hop le");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "Token hop le");
        response.put("username", jwtTokenProvider.getUsernameFromToken(token));
        response.put("accountId", jwtTokenProvider.getAccountIdFromToken(token));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();
        response.put("success", true);
        response.put("message", "Dang xuat thanh cong");
        return ResponseEntity.ok(response);
    }

    private String resolveAuthenticatedAccountId(Authentication authentication, HttpSession session) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof btl.nongnghiep.authentication.security.UserDetailsImpl user) {
            return user.getId();
        }

        return session != null ? (String) session.getAttribute(AuthService.SESSION_ACCOUNT_ID) : null;
    }

    private String extractAccountId(Map<String, Object> requestBody) {
        Object accountId = requestBody.get("accountId");
        if (accountId == null) {
            accountId = requestBody.get("idAccount");
        }
        return accountId != null ? String.valueOf(accountId) : null;
    }
}

