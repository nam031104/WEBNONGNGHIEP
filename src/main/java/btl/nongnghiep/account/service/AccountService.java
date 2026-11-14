package btl.nongnghiep.account.service;

import btl.nongnghiep.account.entity.Account;
import btl.nongnghiep.account.repository.AccountRepository;
import btl.nongnghiep.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account findAccountByUserName(String userName){
        return accountRepository.findByUsername(userName).
                orElseThrow(()-> new NotFoundException("Khong tim thay tai khoan"));
    }

    public Account findById(String id){
        return accountRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Khong tim thay tai khoan"));
    }
}
