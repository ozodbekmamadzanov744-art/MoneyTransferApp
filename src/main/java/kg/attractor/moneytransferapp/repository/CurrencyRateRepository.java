package kg.attractor.moneytransferapp.repository;

import kg.attractor.moneytransferapp.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, String> {
}
