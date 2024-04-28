package com.grid.and.cloud.api.database.interaction.services.interfaces;

import com.grid.and.cloud.api.database.interaction.dao.interfaces.DAO;
import com.grid.and.cloud.api.database.objects.interfaces.DTO;
import com.grid.and.cloud.api.tools.IterableTools;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class ParentService <Model extends DTO<Model>, Dto extends DTO<Dto>> implements Service<Dto> {
    protected interface Handler<T> { void handle(T handledObject); }
    protected final DAO<Model> dao;
    protected final int batchSize;
    protected final Logger logger = Logger.getLogger(this.getClass());

    public ParentService(DAO<Model> dao, int batchSize) {
        this.dao = dao;
        this.batchSize = batchSize;
    }

    protected abstract void addValidation(Dto object);
    protected abstract void updateValidation(Dto object);
    protected abstract void removeValidation(Dto object);
    protected abstract List<Model> toModelsList(Iterable<Dto> dtos);
    protected abstract Map<Integer, Model> toModelsMap(Iterable<Dto> dtos);
    protected abstract List<Dto> toDTOsList(Iterable<Model> models);
    protected abstract Map<Integer, Dto> toDTOsMap(Iterable<Model> models);

    protected void beforeAddingActions(Iterable<Dto> dtos) {};
    protected void beforeUpdatingActions(Iterable<Dto> dtos) {};
    protected void beforeRemovingActions(Iterable<Dto> dtos) {};
    protected void afterAddingActions(Iterable<Dto> dtos) {};
    protected void afterUpdatingActions(Iterable<Dto> dtos) {};
    protected void afterRemovingActions(Iterable<Dto> dtos) {};

    public List<Dto> getByFields(Iterable<Dto> objects) {
        if (IterableTools.size(objects) == 0)
            return new LinkedList<>();
        return toDTOsList(dao.getByFields(toModelsList(objects)));
    }

    public void addToDatabase(Dto object) {
        addToDatabase(List.of(object));
    }

    public void addToDatabase(Iterable<Dto> objects) {
        int size = IterableTools.size(objects);
        if (size == 0)
            return;
        if (size > batchSize) {
            addToDatabase(objects, batchSize);
            return;
        }

        validate(objects, this::addValidation);
        beforeAddingActions(objects);

        List<Model> modelsToAdd = toModelsList(objects);
        List<Model> foundModels = dao.getByFields(modelsToAdd);
        if (!foundModels.isEmpty())
            throw new IllegalArgumentException("Account for such person already exists");

        dao.save(modelsToAdd);

        afterAddingActions(objects);
    }

    protected void addToDatabase(Iterable<Dto> objects, int batchSize) {
        String formatMessage = "%d/%d objects has added to database";
        batchAction(objects, batchSize, this::addToDatabase, formatMessage);
    }

    public void updateInDatabase(Dto object) { updateInDatabase(List.of(object)); }

    public void updateInDatabase(Iterable<Dto> objects) {
        int size = IterableTools.size(objects);
        if (size == 0)
            return;
        if (size > batchSize) {
            updateInDatabase(objects, batchSize);
            return;
        }

        validate(objects, this::updateValidation);
        beforeUpdatingActions(objects);

        Set<Integer> allIds = StreamSupport.stream(objects.spliterator(), false).map(Dto::getId).collect(Collectors.toSet());
        List<Model> foundModels = dao.getById(allIds);

        if (foundModels.size() != allIds.size()) {
            List<Integer> foundIds = foundModels.stream().map(Model::getId).toList();
            Set<Integer> notFound = IterableTools.minus(allIds, foundIds);
            throw new IllegalArgumentException("Models with such ids not found in database: " + notFound);
        }

        dao.update(toModelsMap(objects).values());

        afterUpdatingActions(objects);
    }

    protected void updateInDatabase(Iterable<Dto> objects, int batchSize) {
        String formatMessage = "%d/%d objects has updated in database";
        batchAction(objects, batchSize, this::updateInDatabase, formatMessage);
    }

    public void removeFromDatabase(Dto object) {
        removeFromDatabase(List.of(object));
    }

    public void removeFromDatabase(Iterable<Dto> objects) {
        int size = IterableTools.size(objects);
        if (size == 0)
            return;
        if (size > batchSize) {
            removeFromDatabase(objects, batchSize);
            return;
        }

        validate(objects, this::removeValidation);
        beforeRemovingActions(objects);

        Map<Integer, Model> modelsToRemove = toModelsMap(objects);
        List<Model> foundModels = dao.getById(modelsToRemove.keySet());

        List<Integer> foundIds = foundModels.stream().map(Model::getId).toList();
        dao.delete(foundIds);

        afterRemovingActions(objects);
    }

    protected void removeFromDatabase(Iterable<Dto> objects, int batchSize) {
        String formatMessage = "%d/%d objects has removed from database";
        batchAction(objects, batchSize, this::removeFromDatabase, formatMessage);
    }

    public List<Dto> getAll() {
        return toDTOsList(dao.getAll());
    }

    public Map<Integer, Dto> getMap() {
        return toDTOsMap(dao.getAll());
    }

    public Map<Integer, Dto> getMap(Iterable<Integer> ids) {
        int size = IterableTools.size(ids);
        if (size == 0)
            return new TreeMap<>();
        if (size > batchSize)
            return getMap(ids, batchSize);

        validate(ids, this::fieldValidation);
        List<Model> foundModels = dao.getById(ids);
        return toDTOsMap(foundModels);
    }

    protected Map<Integer, Dto> getMap(Iterable<Integer> ids, int batchSize) {
        String formatMessage = "%d/%d objects has extracted from database";
        MapHandler handler = new MapHandler();
        batchAction(ids, batchSize, handler, formatMessage);
        return handler.getMap();
    }

    protected class MapHandler implements Handler<Iterable<Integer>> {
        private final Map<Integer, Dto> map = new TreeMap<>();
        @Override
        public void handle(Iterable<Integer> ids) {
            this.map.putAll(ParentService.this.getMap(ids));
        }
        public Map<Integer, Dto> getMap() {
            return map;
        }
    }

    protected <T> void batchAction(Iterable<T> handledObjects, int batchSize, Handler<Iterable<T>> handler, String formatMessage) {
        int size = IterableTools.size(handledObjects);
        if (size == 0)
            return;

        int i = 1;
        LinkedList<T> batch = new LinkedList<>();
        for (T object : handledObjects) {
            batch.add(object);
            if (i % batchSize == 0) {
                logger.debug(String.format(formatMessage, i, size));
                handler.handle(batch);
                batch.clear();
            }
            i++;
        }
        logger.debug(String.format(formatMessage, i, size));
        handler.handle(batch);
    }

    protected <T> void fieldValidation(T field) {
        if (field == null)
            throw new IllegalArgumentException("Field should not be null");
    };

    protected <T> void validate(Iterable<T> objects, Handler<T> validator) {
        if (objects == null)
            throw new IllegalArgumentException("Objects list should not be null");
        for (T object : objects)
            validator.handle(object);
    }
}
