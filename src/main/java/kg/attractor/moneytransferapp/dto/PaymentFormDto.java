package kg.attractor.moneytransferapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentFormDto {
    @NotNull(message = "{error.account}")
    @Min(value = 100000, message = "{error.account}")
    @Max(value = 999999, message = "{error.account}")
    private Long sourceId;
    @NotNull(message = "{error.subscriber}")
    private Long providerId;
    @NotBlank(message = "{error.subscriber}")
    @Size(max = 40, message = "{error.subscriber}")
    private String reference;
    @NotNull(message = "{error.amount}")
    @DecimalMin(value = "0.01", message = "{error.amount}")
    @DecimalMax(value = "1000000000", message = "{error.amount}")
    @Digits(integer = 10, fraction = 2, message = "{error.amount}")
    private BigDecimal amount;
}
