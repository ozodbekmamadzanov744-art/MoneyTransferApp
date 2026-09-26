package kg.attractor.moneytransferapp.security;

import kg.attractor.moneytransferapp.model.AppUser;
import kg.attractor.moneytransferapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("error.user"));
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
    }
}
