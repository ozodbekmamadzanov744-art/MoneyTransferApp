package kg.attractor.moneytransferapp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private String type;
    private String status;
    private BigDecimal amount;
    private String currency;
    private BigDecimal creditedAmount;
    private String creditedCurrency;
    private BigDecimal rate;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String createdLabel;
    private String processedLabel;
    private Long sourceAccountId;
    private String senderUsername;
    private Long targetAccountId;
    private String recipientUsername;
    private String providerName;
    private String subscriberReference;
    private String reviewerUsername;
    private String failureKey;
}
