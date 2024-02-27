package com.example.myconfig_test;

import com.example.entity.Account;

public class MyCustomRegisteredAccount {
    private int status;
    private Account account;
    
    public MyCustomRegisteredAccount(int status, Account account) {
        this.status = status;
        this.account = account;
    }

    public int getStatus() {
        return status;
    }

    public Account getAccount() {
        return account;
    }
    
}
