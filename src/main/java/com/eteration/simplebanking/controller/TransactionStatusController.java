package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TransactionStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionStatusController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionStatusController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


}
