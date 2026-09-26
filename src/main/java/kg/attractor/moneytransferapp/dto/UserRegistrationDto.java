package kg.attractor.moneytransferapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "{error.username}")
    @Pattern(regexp = "[a-zA-Z0-9_]{3,40}", message = "{error.username}")
    private String username;
    @NotBlank(message = "{error.password}")
    @Size(min = 6, max = 72, message = "{error.password}")
    private String password;
}
