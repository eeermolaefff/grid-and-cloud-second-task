package com.grid.and.cloud.api.database.objects.interfaces;

import lombok.NonNull;

public interface DTO<T> extends Identifiable, Comparable<T> {
    void updateFields(@NonNull T newObject);
}
