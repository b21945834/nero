package tr.com.nero.stock.barcode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BarcodeService {
    private final BarcodeRepository barcodeRepository;

    public void deleteByStockId(Long stockId) {
        barcodeRepository.deleteByStockId(stockId);
    }

}
