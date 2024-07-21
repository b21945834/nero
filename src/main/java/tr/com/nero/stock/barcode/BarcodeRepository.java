package tr.com.nero.stock.barcode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BarcodeRepository extends JpaRepository<Barcode, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Barcode b WHERE b.stock.id = :stockId")
    void deleteByStockId(@Param("stockId") Long stockId);
}
