package kg.attractor.moneytransferapp.service.impl;

import kg.attractor.moneytransferapp.service.UserService;
import kg.attractor.moneytransferapp.dto.UserRegistrationDto;
import kg.attractor.moneytransferapp.mapper.UserMapper;
import kg.attractor.moneytransferapp.model.AppUser;
import kg.attractor.moneytransferapp.repository.UserRepository;
import kg.attractor.moneytransferapp.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    
    @Transactional
    public void register(UserRegistrationDto dto) {
        AppUser user = UserMapper.toModel(dto);
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || !username.matches("[a-zA-Z0-9_]{3,40}")) throw new BusinessException("error.username");
        if (password == null || password.length() < 6 || password.length() > 72 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) throw new BusinessException("error.password");
        if (users.existsByUsername(username)) throw new BusinessException("error.usernameExists");
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");
        users.saveAndFlush(user);
        log.info("User registered: {}", username);
    }
}
