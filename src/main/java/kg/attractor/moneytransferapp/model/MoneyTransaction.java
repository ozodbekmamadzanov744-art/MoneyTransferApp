package kg.attractor.moneytransferapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "money_transactions")
public class MoneyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String type;
    @Column(nullable = false, length = 20)
    private String status;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Account source;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Account target;
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;
    @Column(nullable = false, precision = 19, scale = 2)
    private java.math.BigDecimal amount;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false, precision = 19, scale = 2)
    private java.math.BigDecimal creditedAmount;
    @Column(nullable = false, length = 3)
    private String creditedCurrency;
    @Column(nullable = false, precision = 19, scale = 8)
    private java.math.BigDecimal rate;
    @Column(nullable = false)
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime processedAt;
    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private AppUser reviewedBy;
    @Column(length = 80)
    private String failureKey;
    
    public String getCreatedLabel() {
        return createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
    
    public String getProcessedLabel() {
        return processedAt == null ? "\u2014" : processedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
