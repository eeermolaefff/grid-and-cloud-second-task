package com.grid.and.cloud.api.database.objects.models;

import com.grid.and.cloud.api.database.objects.basic.AccountBasic;
import com.grid.and.cloud.api.database.objects.dto.AccountDTO;
import lombok.*;

import java.util.Comparator;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class AccountModel extends AccountBasic<AccountModel> {

    public AccountModel(@NonNull AccountDTO dto) {
        super(dto);
    }

    @Override
    public void updateFields(@NonNull AccountModel newObject) {
        super.basicUpdateFields(newObject);
    }

    @Override
    public int compareTo(@NonNull AccountModel o) {
        return super.basicComparator(o).compare(this, o);
    }
}
