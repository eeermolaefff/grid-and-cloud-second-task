package com.grid.and.cloud.api.database.interaction.dao;

import com.grid.and.cloud.api.database.interaction.dao.interfaces.ParentDAO;
import com.grid.and.cloud.api.tools.IterableTools;
import com.grid.and.cloud.api.tools.Pair;
import com.grid.and.cloud.api.database.objects.models.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AccountDAO extends ParentDAO<AccountModel> {
    @Autowired
    public AccountDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected String getTableName() {
        return "accounts";
    };

    @Override
    protected BeanPropertyRowMapper<AccountModel> getBeanPropertyRowMapper() {
        return new BeanPropertyRowMapper<>(AccountModel.class);
    }

    @Override
    protected void getValidation(AccountModel model) {
        if (model == null)
            throw new IllegalArgumentException("Model should not be null");
        if (model.getName() == null)
            throw new IllegalArgumentException("Model name should not be null: " + model);
        if (model.getSurname() == null)
            throw new IllegalArgumentException("Model surname should not be null: " + model);
    }
    @Override
    protected void saveValidation(AccountModel model) {
        getValidation(model);
        if (model.getBalance() == null)
            throw new IllegalArgumentException("Model balance should not be null: " + model);
    }

    @Override
    protected void updateValidation(AccountModel model) {
        saveValidation(model);
        if (model.getId() == null)
            throw new IllegalArgumentException("Model id should not be null: " + model);
    }

    public List<AccountModel> getByFields(Iterable<AccountModel> models) {
        validate(models, this::getValidation);

        List<String> names = new LinkedList<>();
        List<String> surnames = new LinkedList<>();
        for (AccountModel model : models) {
            names.add(model.getName());
            surnames.add(model.getSurname());
        }

        return getByFields(
                new Pair<>(names, "name"),
                new Pair<>(surnames, "surname")
        );
    }

    public void save(Iterable<AccountModel> models) {
        validate(models, this::saveValidation);

        List<AccountModel> foundModels = getByFields(models);
        if (!foundModels.isEmpty()) {
            String message = "Objects with such fields already exist in database: " + foundModels;
            throw new IllegalArgumentException(message);
        }

        List<String> names = new LinkedList<>();
        List<String> surnames = new LinkedList<>();
        List<Double> balances = new LinkedList<>();
        for (AccountModel model : models) {
            names.add(model.getName());
            surnames.add(model.getSurname());
            balances.add(model.getBalance());
        }

        saveByFields(
                new Pair<>(names, "name"),
                new Pair<>(surnames, "surname"),
                new Pair<>(balances, "balance")
        );
    }

    public void update(Iterable<AccountModel> models) {
        validate(models, this::updateValidation);

        List<Integer> ids = new LinkedList<>();
        List<String> names = new LinkedList<>();
        List<String> surnames = new LinkedList<>();
        List<Double> balances = new LinkedList<>();
        for (AccountModel model : models) {
            ids.add(model.getId());
            names.add(model.getName());
            surnames.add(model.getSurname());
            balances.add(model.getBalance());
        }

        List<Integer> foundIds = getById(ids).stream().map(AccountModel::getId).toList();
        if (foundIds.size() != ids.size()) {
            var notFound = IterableTools.minus(ids, foundIds);
            String message = "Objects with such ids not found in database:";
            message += "notFound=" + notFound + " models=" + models;
            throw new IllegalArgumentException(message);
        }

        updateByFields(
                new Pair<>(names, "name"),
                new Pair<>(surnames, "surname"),
                new Pair<>(balances, "balance"),
                new Pair<>(ids, "id")
        );
    }
}
