package tr.com.nero.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
    List<Stock> findByUserId(Long userId);
    Optional<Stock> findByIdAndUserId(Long id, Long userId);
}
