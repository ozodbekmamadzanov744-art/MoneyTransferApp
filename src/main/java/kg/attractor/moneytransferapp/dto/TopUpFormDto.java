package kg.attractor.moneytransferapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TopUpFormDto {
    @NotNull(message = "{error.account}")
    @Min(value = 100000, message = "{error.account}")
    @Max(value = 999999, message = "{error.account}")
    private Long accountId;
    @NotNull(message = "{error.amount}")
    @DecimalMin(value = "0.01", message = "{error.amount}")
    @DecimalMax(value = "1000000000", message = "{error.amount}")
    @Digits(integer = 10, fraction = 2, message = "{error.amount}")
    private BigDecimal amount;
}
