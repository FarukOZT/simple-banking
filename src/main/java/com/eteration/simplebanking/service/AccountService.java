package com.eteration.simplebanking.service;

import com.eteration.simplebanking.constant.Action;
import com.eteration.simplebanking.constant.Status;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.repository.AccountRepository;
import com.eteration.simplebanking.repository.DepositTransactionRepository;
import com.eteration.simplebanking.repository.TransactionRepository;
import com.eteration.simplebanking.repository.WithdrawalTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final WithdrawalTransactionRepository withdrawalTransactionRepository;
    private final DepositTransactionRepository depositTransactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, WithdrawalTransactionRepository withdrawalTransactionRepository, DepositTransactionRepository depositTransactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.withdrawalTransactionRepository = withdrawalTransactionRepository;
        this.depositTransactionRepository = depositTransactionRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(Long id) {
        if(accountRepository.findById(id).isPresent()){
            return accountRepository.findById(id);
        }
        return null;
    }

    @Transactional
    public Transaction deposit(Long id, double amount) {
        Transaction transaction = new Transaction();
        DepositTransaction depositTransaction = new DepositTransaction();
        transaction.setInitiationDate(LocalDateTime.now());
        Optional<Account> account = getAccount(id);
        account.get().setBalance(account.get().getBalance() + amount);
        accountRepository.save(account.get());
        depositTransaction.setAmount(amount);
        depositTransaction.setAction(Action.DEPOSIT.toString());
        transaction.setAccount(account.get());
        transaction.setStatus(Status.SUCCESS.toString());
        transaction.setCompletionDate(LocalDateTime.now());
        depositTransaction.setTransaction(transaction);
        transactionRepository.save(transaction);
        depositTransactionRepository.save(depositTransaction);
        return transaction;
    }
    @Transactional
    public Transaction withdraw(Long id, double amount) throws InsufficientBalanceException {
        Transaction transaction = new Transaction();
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
        transaction.setInitiationDate(LocalDateTime.now());
        Optional<Account> account = getAccount(id);
        if (account.get().getBalance() >= amount) {
            account.get().setBalance(account.get().getBalance() - amount);
            accountRepository.save(account.get());
            withdrawalTransaction.setAmount(amount);
            withdrawalTransaction.setAction(Action.WITHDRAWAL.toString());
            transaction.setAccount(account.get());
            transaction.setStatus(Status.SUCCESS.toString());
            transaction.setCompletionDate(LocalDateTime.now());
            withdrawalTransaction.setTransaction(transaction);
            transactionRepository.save(transaction);
            withdrawalTransactionRepository.save(withdrawalTransaction);
        } else {
            throw new InsufficientBalanceException("Insufficient funds");
        }
        return transaction;
    }
}

