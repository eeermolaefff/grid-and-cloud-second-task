package com.grid.and.cloud.api.database.interaction;

import com.grid.and.cloud.api.database.interaction.services.AccountService;
import com.grid.and.cloud.api.database.objects.dto.AccountDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.StampedLock;

@Service
public class DatabaseService {
    private final Logger logger = Logger.getLogger(this.getClass());
    private final StampedLock lock = new StampedLock();
    private final AccountService accountService;

    @Autowired
    public DatabaseService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<AccountDTO> getAllAccounts() {
        long stamp = lock.readLock();
        try {
            return accountService.getAll();
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public Map<Integer, AccountDTO> getClientAccountsByIds(Iterable<Integer> accountIds) {
        long stamp = lock.readLock();
        try {
            return accountService.getMap(accountIds);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Transactional
    public void addNewAccounts(Iterable<AccountDTO> accounts) {
        long stamp = lock.writeLock();
        try {
            accountService.addToDatabase(accounts);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Transactional
    public void updateOldAccounts(Iterable<AccountDTO> accounts) {
        long stamp = lock.writeLock();
        try {
            accountService.updateInDatabase(accounts);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Transactional
    public void removeOldAccounts(Iterable<AccountDTO> accounts) {
        long stamp = lock.writeLock();
        try {
            accountService.removeFromDatabase(accounts);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}