package kg.attractor.moneytransferapp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyRateResponseDto {
    private String code;
    private BigDecimal unitsPerUsd;
}
