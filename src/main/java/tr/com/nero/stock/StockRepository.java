package tr.com.nero.stock;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    Page<Stock> findAll(Specification<Stock> spec, Pageable pageable);


    @Query("SELECT s FROM Stock s WHERE s.user.id = :userId AND s.id = :stockId")
    Optional<Stock> findDetailsById(@Param("userId") Long userId, @Param("stockId") Long stockId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Stock s WHERE s.user.id = :userId AND s.id = :stockId")
    void deleteByIdAndUserId(@Param("userId") Long userId, @Param("stockId") Long stockId);
}
