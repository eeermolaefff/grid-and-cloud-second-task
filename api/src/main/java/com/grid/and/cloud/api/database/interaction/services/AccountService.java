package com.grid.and.cloud.api.database.interaction.services;

import com.grid.and.cloud.api.database.objects.dto.AccountDTO;
import com.grid.and.cloud.api.database.interaction.dao.AccountDAO;
import com.grid.and.cloud.api.database.interaction.services.interfaces.ParentService;
import com.grid.and.cloud.api.database.objects.models.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AccountService extends ParentService<AccountModel, AccountDTO> {

    @Autowired
    public AccountService(
            AccountDAO accountDAO,
            @Value("${database.batch.size}") int batchSize
    ) {
        super(accountDAO, batchSize);
    }

    @Override
    protected List<AccountModel> toModelsList(Iterable<AccountDTO> dtos) {
        List<AccountModel> models = new LinkedList<>();
        for (AccountDTO dto : dtos)
            models.add(new AccountModel(dto));
        return models;
    }

    @Override
    protected Map<Integer, AccountModel> toModelsMap(Iterable<AccountDTO> dtos) {
        Map<Integer, AccountModel> models = new TreeMap<>();
        for (AccountDTO dto : dtos) {
            if (dto.getId() == null)
                throw new IllegalArgumentException("Null dto.getId() value while adding to map: " + dto);
            models.put(dto.getId(), new AccountModel(dto));
        }
        return models;
    }

    @Override
    protected List<AccountDTO> toDTOsList(Iterable<AccountModel> models) {
        List<AccountDTO> dtos = new LinkedList<>();
        for (AccountModel model : models)
            dtos.add(new AccountDTO(model));
        return dtos;
    }

    @Override
    protected Map<Integer, AccountDTO> toDTOsMap(Iterable<AccountModel> models) {
        Map<Integer, AccountDTO> dtos = new TreeMap<>();
        for (AccountModel model : models) {
            if (model.getId() == null)
                throw new IllegalArgumentException("Null model.getId() value while adding to map: " + model);
            dtos.put(model.getId(), new AccountDTO(model));
        }
        return dtos;
    }

    @Override
    protected void addValidation(AccountDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("DTO should not be null");
        if (dto.getName() == null || dto.getSurname() == null || dto.getBalance() == null) {
            String message = "DTO name, surname and balance should not be null: " + dto;
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    protected void updateValidation(AccountDTO dto) {
        addValidation(dto);
        if (dto.getId() == null) {
            throw new IllegalArgumentException("DTO id should not be null: " + dto);
        }
    }

    @Override
    protected void removeValidation(AccountDTO dto) {
        updateValidation(dto);
    }
}
