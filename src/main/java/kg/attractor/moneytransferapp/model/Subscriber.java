package kg.attractor.moneytransferapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscribers", uniqueConstraints = @UniqueConstraint(columnNames = {"provider_id", "reference"}))
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;
    @Column(nullable = false, length = 40)
    private String reference;
    @Column(nullable = false, precision = 19, scale = 2)
    private java.math.BigDecimal balance;
}
