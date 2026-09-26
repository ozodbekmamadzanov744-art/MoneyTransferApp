package kg.attractor.moneytransferapp.mapper;

import kg.attractor.moneytransferapp.dto.TransactionResponseDto;
import kg.attractor.moneytransferapp.model.Account;
import kg.attractor.moneytransferapp.model.MoneyTransaction;
import kg.attractor.moneytransferapp.model.Subscriber;

public class TransactionMapper {
    
    public static TransactionResponseDto toDto(MoneyTransaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setStatus(transaction.getStatus());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setCreditedAmount(transaction.getCreditedAmount());
        dto.setCreditedCurrency(transaction.getCreditedCurrency());
        dto.setRate(transaction.getRate());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setProcessedAt(transaction.getProcessedAt());
        dto.setCreatedLabel(transaction.getCreatedLabel());
        dto.setProcessedLabel(transaction.getProcessedLabel());
        dto.setFailureKey(transaction.getFailureKey());
        
        Account source = transaction.getSource();
        if (source != null) {
            dto.setSourceAccountId(source.getId());
            dto.setSenderUsername(source.getUser().getUsername());
        }
        
        Account target = transaction.getTarget();
        if (target != null) {
            dto.setTargetAccountId(target.getId());
            dto.setRecipientUsername(target.getUser().getUsername());
        }
        
        Subscriber subscriber = transaction.getSubscriber();
        if (subscriber != null) {
            dto.setProviderName(subscriber.getProvider().getName());
            dto.setSubscriberReference(subscriber.getReference());
        }
        
        if (transaction.getReviewedBy() != null) {
            dto.setReviewerUsername(transaction.getReviewedBy().getUsername());
        }
        return dto;
    }
}
