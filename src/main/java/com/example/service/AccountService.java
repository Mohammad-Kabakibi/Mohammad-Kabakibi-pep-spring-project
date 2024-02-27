package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account){
        if(!account.getUsername().isBlank() && account.getPassword().length() > 3){
            Account test_account = accountRepository.findByUsername(account.getUsername());
            if(test_account == null){
                return accountRepository.save(account);
            }
        }
        return null;
    }

    public Account loginAccount(Account account){
        Account test_account = accountRepository.findByUsername(account.getUsername());
        if(test_account != null){
            if(test_account.getPassword().equals(account.getPassword())){
                return test_account;
            }
        }
        return null;
    }

    public boolean isRealUser(int user_id){
        if(user_id >= 0){
            Optional<Account> test_account = accountRepository.findById(user_id);
            return test_account.isPresent();
        }
        return false;
    }
}
