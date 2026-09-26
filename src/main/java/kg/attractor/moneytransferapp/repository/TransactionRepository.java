package kg.attractor.moneytransferapp.repository;

import kg.attractor.moneytransferapp.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface TransactionRepository extends JpaRepository<MoneyTransaction, Long> {
    
    @Query("""
            select t
            from MoneyTransaction t
            left join t.source s
            left join s.user sender
            left join t.target d
            left join d.user recipient
            where sender.username = :username
               or recipient.username = :username
            order by t.createdAt desc, t.id desc
            """)
    List<MoneyTransaction> findHistory(@Param("username")
    String username);
    
    List<MoneyTransaction> findAllByOrderByCreatedAtDescIdDesc();
    
    List<MoneyTransaction> findByStatusOrderByCreatedAtDescIdDesc(String status);
}
