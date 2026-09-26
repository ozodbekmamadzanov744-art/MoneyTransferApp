package kg.attractor.moneytransferapp.mapper;

import kg.attractor.moneytransferapp.dto.UserRegistrationDto;
import kg.attractor.moneytransferapp.model.AppUser;

public class UserMapper {
    
    public static AppUser toModel(UserRegistrationDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
