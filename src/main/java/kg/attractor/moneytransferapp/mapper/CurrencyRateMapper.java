package kg.attractor.moneytransferapp.mapper;

import kg.attractor.moneytransferapp.dto.CurrencyRateResponseDto;
import kg.attractor.moneytransferapp.model.CurrencyRate;

public class CurrencyRateMapper {
    
    public static CurrencyRateResponseDto toDto(CurrencyRate rate) {
        CurrencyRateResponseDto dto = new CurrencyRateResponseDto();
        dto.setCode(rate.getCode());
        dto.setUnitsPerUsd(rate.getUnitsPerUsd());
        return dto;
    }
}
