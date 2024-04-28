package com.grid.and.cloud.api.database.objects.basic;

import com.grid.and.cloud.api.database.objects.interfaces.ParentBasic;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public abstract class AccountBasic<P extends AccountBasic<P>> extends ParentBasic<P> {
    protected Integer id;
    protected String name;
    protected String surname;
    protected Double balance;
    protected String updatedAt;

    public AccountBasic(@NonNull AccountBasic<?> basic) {
        id = basic.id;
        name = basic.name;
        surname = basic.surname;
        balance = basic.balance;
        updatedAt = basic.updatedAt;
    }

    @Override
    public void basicUpdateFields(@NonNull P newObject) {
        if (newObject.id != null)           id = newObject.id;
        if (newObject.name != null)         name = newObject.name;
        if (newObject.surname != null)      surname = newObject.surname;
        if (newObject.balance != null)      balance = newObject.balance;
        if (newObject.updatedAt != null)    updatedAt = newObject.updatedAt;
    }

    @Override
    public Comparator<P> basicComparator(@NonNull P obj) {
        return Comparator.comparing(P::getName)
                .thenComparing(P::getSurname);
    }
}