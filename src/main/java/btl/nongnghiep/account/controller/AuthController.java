package btl.nongnghiep.account.controller;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.repository.AccountRepository;
import btl.nongnghiep.account.security.JwtUtils;
import btl.nongnghiep.mqtt.Mqtt;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final boolean cookieSecure;

    public AuthController(AuthenticationManager authenticationManager,
                          AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          Mqtt mqtt,
                          @Value("${app.jwt.cookie-secure:false}") boolean cookieSecure) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.cookieSecure = cookieSecure;
    }

    @GetMapping("/login")
    public String loginPage() {

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", new Account());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("account") Account account,
                           RedirectAttributes redirectAttributes) {
        sanitizeAccount(account);

        if (isBlank(account.getUsername()) || isBlank(account.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Username va password khong duoc de trong.");
            redirectAttributes.addFlashAttribute("account", account);
            return "redirect:/register";
        }

        if (accountRepository.existsByUsername(account.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username da ton tai.");
            redirectAttributes.addFlashAttribute("account", account);
            return "redirect:/register";
        }

        if (!isBlank(account.getEmail()) && accountRepository.existsByEmail(account.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email da duoc su dung.");
            redirectAttributes.addFlashAttribute("account", account);
            return "redirect:/register";
        }

        account.setIdAccount(null);
        account.setRole("USER");
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        redirectAttributes.addFlashAttribute("success", "Dang ky thanh cong. Vui long dang nhap.");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        String normalizedUsername = normalize(username);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedUsername, password));

            Account account = accountRepository.findByUsername(normalizedUsername)
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            String token = jwtUtils.generateToken(account);
            ResponseCookie cookie = ResponseCookie.from(JwtUtils.COOKIE_NAME, token)
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofMillis(jwtUtils.getJwtExpirationMs()))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return "redirect:" + resolveRedirectPath(account);
        } catch (AuthenticationException ex) {
            redirectAttributes.addFlashAttribute("error", "Sai username hoac password.");
            return "redirect:/login?error";
        }
    }

    private String resolveRedirectPath(Account account) {
        return "ADMIN".equals(account.getNormalizedRole()) ? "/admin/home" : "/user/home";
    }

    private void sanitizeAccount(Account account) {
        account.setUsername(normalize(account.getUsername()));
        account.setName(normalize(account.getName()));
        account.setEmail(normalize(account.getEmail()));
        account.setNumberPhone(normalize(account.getNumberPhone()));
        account.setLocation(normalize(account.getLocation()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
