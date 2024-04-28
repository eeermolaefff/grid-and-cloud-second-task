package com.grid.and.cloud.api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grid.and.cloud.api.database.interaction.DatabaseService;
import com.grid.and.cloud.api.database.objects.dto.AccountDTO;
import com.grid.and.cloud.api.database.objects.dto.TransferDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://frontend.grid-and-cloud-bank-simulator.ru", "http://localhost:3000/"})
@RestController
class DatabaseController {
    private final Logger logger = Logger.getLogger(this.getClass());
    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/accounts/get/all")
    public List<AccountDTO> getAllAccounts() {
        return databaseService.getAllAccounts();
    }

    @GetMapping("/accounts/get/{id}")
    public AccountDTO getAccountById(@PathVariable Integer id) {
        Map<Integer, AccountDTO> accounts = databaseService.getClientAccountsByIds(List.of(id));
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    @PostMapping("/accounts/add")
    public Map<String, Object> addNewAccount(@RequestBody AccountDTO newAccount) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("ADDING: " + newAccount);
            databaseService.addNewAccounts(List.of(newAccount));
            logger.info("ADDED: " + newAccount);
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Problem during adding new account", e);
            response.put("status", "failed");
            response.put("reason", e.getMessage());
        }

        return response;
    }

    @PostMapping("/accounts/delete")
    public Map<String, Object> deleteOldAccount(@RequestBody AccountDTO oldAccount) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("REMOVING: " + oldAccount);
            databaseService.removeOldAccounts(List.of(oldAccount));
            logger.info("REMOVED: " + oldAccount);
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Problem during adding new account", e);
            response.put("status", "failed");
            response.put("reason", e.getMessage());
        }

        return response;
    }

    @PostMapping("/transfer")
    public Map<String, Object> transfer(@RequestBody TransferDTO transfer) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("TRANSFERRING: " + transfer);

            AccountDTO[] foundAccounts = getAccountsFromDatabase(transfer);
            AccountDTO accountFrom = foundAccounts[0], accountTo = foundAccounts[1];

            validateTransaction(accountFrom, accountTo, transfer);

            accountFrom.setBalance(accountFrom.getBalance() - transfer.getAmount());
            accountTo.setBalance(accountTo.getBalance() + transfer.getAmount());

            databaseService.updateOldAccounts(List.of(accountFrom, accountTo));

            logger.info("TRANSFERRED: " + transfer);
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Problem during transferring", e);
            response.put("status", "failed");
            response.put("reason", e.getMessage());
        }

        return response;
    }

    private AccountDTO[] getAccountsFromDatabase(
            TransferDTO transfer
    ) {
        AccountDTO[] accounts = new AccountDTO[2];
        Map<Integer, AccountDTO> foundAccounts = databaseService.getClientAccountsByIds(
                List.of(transfer.getAccountIdFrom(), transfer.getAccountIdTo())
        );

        for (var accountEntry : foundAccounts.entrySet()) {
            if (accountEntry.getKey().equals(transfer.getAccountIdFrom()))
                accounts[0] = accountEntry.getValue();
            if (accountEntry.getKey().equals(transfer.getAccountIdTo()))
                accounts[1] = accountEntry.getValue();
        }

        return accounts;
    }

    private void validateTransaction(
            AccountDTO accountFrom,
            AccountDTO accountTo,
            TransferDTO transfer
    ) {
        if (accountFrom.getId() == null)
            throw new IllegalArgumentException("Sender account not found");
        if (accountTo.getId() == null)
            throw new IllegalArgumentException("Recipient account not found");
        if (accountTo.getId().equals(accountFrom.getId()))
            throw new IllegalArgumentException("Can not transfer to yourself");
        if (accountFrom.getBalance() < transfer.getAmount())
            throw new IllegalArgumentException("Insufficient balance");
    }
}