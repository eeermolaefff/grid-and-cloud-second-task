package com.grid.and.cloud.api.database.objects.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class TransferDTO {
    private Integer accountIdFrom;
    private Integer accountIdTo;
    private Double amount;
}