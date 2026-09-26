package kg.attractor.moneytransferapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {
    @Id
    @Column(length = 3)
    private String code;
    @Column(nullable = false, precision = 19, scale = 6)
    private java.math.BigDecimal unitsPerUsd;
}
