package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/credit")
    public Transaction deposit(@RequestBody DepositTransaction depositTransaction) {
        return accountService.deposit(depositTransaction.getTransaction().getAccount().getId(), depositTransaction.getAmount());
    }

    @PostMapping("/debit")
    public Transaction withdraw(@RequestBody WithdrawalTransaction withdrawalTransaction) throws InsufficientBalanceException {
        return accountService.withdraw(withdrawalTransaction.getTransaction().getAccount().getId(), withdrawalTransaction.getAmount());
    }

}
