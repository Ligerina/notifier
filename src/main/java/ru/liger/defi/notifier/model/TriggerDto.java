package ru.liger.defi.notifier.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TriggerDto {
    public String positionName;
    public String assetName;
    public BigDecimal upperBound;
    public BigDecimal lowerBound;

}
