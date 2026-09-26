package kg.attractor.moneytransferapp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountResponseDto {
    private Long id;
    private String username;
    private String currency;
    private BigDecimal balance;
}
