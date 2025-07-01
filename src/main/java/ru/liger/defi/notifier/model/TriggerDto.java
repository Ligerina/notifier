package ru.liger.defi.notifier.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TriggerDto {
    private UUID id;
    public String positionName;
    public String assetName;
    public BigDecimal upperBound;
    public BigDecimal lowerBound;

}
