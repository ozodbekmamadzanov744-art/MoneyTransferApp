package kg.attractor.moneytransferapp.repository;

import kg.attractor.moneytransferapp.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    List<Account> findByUserUsernameOrderByCurrencyAsc(String username);
    
    boolean existsByUserIdAndCurrency(Long userId, String currency);
    
    long countByUserId(Long userId);
}
