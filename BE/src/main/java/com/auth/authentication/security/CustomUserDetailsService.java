package com.auth.authentication.security;

import com.auth.authentication.entity.Account;
import com.auth.authentication.exception.ResourceNotFoundException;
import com.auth.authentication.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account khong ton tai: " + username));

        return UserDetailsImpl.build(account);
    }

    public UserDetails loadUserById(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account khong ton tai voi id: " + accountId));

        return UserDetailsImpl.build(account);
    }
}
