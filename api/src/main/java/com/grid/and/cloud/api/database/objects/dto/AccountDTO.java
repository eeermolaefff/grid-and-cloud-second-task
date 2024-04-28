package com.grid.and.cloud.api.database.objects.dto;

import com.grid.and.cloud.api.database.objects.basic.AccountBasic;
import com.grid.and.cloud.api.database.objects.models.AccountModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Getter
@Setter
public class AccountDTO extends AccountBasic<AccountDTO> {

    public AccountDTO(@NonNull AccountModel model) {
        super(model);
    }

    @Override
    public void updateFields(@NonNull AccountDTO newObject) {
        super.basicUpdateFields(newObject);
    }

    @Override
    public int compareTo(@NonNull AccountDTO o) {
        return super.basicComparator(o).compare(this, o);
    }
}