package kg.attractor.moneytransferapp.mapper;

import kg.attractor.moneytransferapp.dto.ServiceProviderResponseDto;
import kg.attractor.moneytransferapp.model.ServiceProvider;

public class ServiceProviderMapper {
    
    public static ServiceProviderResponseDto toDto(ServiceProvider provider) {
        ServiceProviderResponseDto dto = new ServiceProviderResponseDto();
        dto.setId(provider.getId());
        dto.setName(provider.getName());
        dto.setCurrency(provider.getCurrency());
        return dto;
    }
}
