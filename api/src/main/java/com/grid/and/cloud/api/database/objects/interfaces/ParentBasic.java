package com.grid.and.cloud.api.database.objects.interfaces;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
public abstract class ParentBasic<P extends Basic<P>> implements Basic<P>, DTO<P> {
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        return compareTo((P) obj) == 0;
    }
}
