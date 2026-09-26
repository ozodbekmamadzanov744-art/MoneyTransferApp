package kg.attractor.moneytransferapp.mapper;

import kg.attractor.moneytransferapp.dto.AccountResponseDto;
import kg.attractor.moneytransferapp.model.Account;

public class AccountMapper {
    
    public static AccountResponseDto toDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setUsername(account.getUser().getUsername());
        dto.setCurrency(account.getCurrency());
        dto.setBalance(account.getBalance());
        return dto;
    }
}
