package kg.attractor.moneytransferapp.repository;

import kg.attractor.moneytransferapp.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    
    Optional<Subscriber> findByProviderIdAndReference(Long providerId, String reference);
    
    List<Subscriber> findAllByOrderByIdAsc();
}
