package com.grid.and.cloud.api.database.objects.interfaces;

import lombok.NonNull;
import java.util.Comparator;

public interface Basic<P extends Basic<P>> {
    void basicUpdateFields(@NonNull P newObject);
    Comparator<P> basicComparator(@NonNull P obj);
}
