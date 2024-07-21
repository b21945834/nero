package tr.com.nero.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.nero.common.NeroUtils;
import tr.com.nero.stock.barcode.Barcode;
import tr.com.nero.stock.barcode.BarcodeService;
import tr.com.nero.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final BarcodeService barcodeService;

    @Transactional
    public Stock createStock(User user, StockRequest stock) {
        //TODO VALIDATE STOCK
        List<Barcode> newBarcodes = new ArrayList<>();
        Stock newStock = Stock.builder()
                .price(stock.getPrice())
                .barcodes(newBarcodes)
                .user(user)
                .brand(stock.getBrand())
                .cost(stock.getCost())
                .vatRate(stock.getVatRate())
                .quantity(stock.getQuantity())
                .category(NeroUtils.convertToCategory(stock.getCategory()))
                .build();
        stock.getBarcodes().forEach(barcode -> {
            newBarcodes.add(new Barcode(barcode, newStock));
        });
        return stockRepository.save(newStock);
    }

    @Transactional
    public Stock update(User user, Long stockId, StockRequest stock) {
        List<Barcode> barcodes = new ArrayList<>();
        Stock newStock = Stock.builder()
                .price(stock.getPrice())
                .barcodes(barcodes)
                .user(user)
                .id(stockId)
                .vatRate(stock.getVatRate())
                .cost(stock.getCost())
                .brand(stock.getBrand())
                .quantity(stock.getQuantity())
                .category(NeroUtils.convertToCategory(stock.getCategory()))
                .build();
        stock.getBarcodes().forEach(string -> {
            barcodes.add(new Barcode(string, newStock));
        });
        barcodeService.deleteByStockId(stockId);
        return stockRepository.save(newStock);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        barcodeService.deleteByStockId(id);
        stockRepository.deleteByIdAndUserId(userId, id);
    }

    public Optional<Stock> getWithId(Long userId, Long id) {
        return stockRepository.findDetailsById(userId, id);
    }

    public List<Stock> getStocksWithFilters(Long userId, List<String> categoryNames, Optional<Double> minPrice, Optional<Double> maxPrice, Pageable pageable) {
        Specification<Stock> spec = Specification.where(StockSpecification.hasUserId(userId));

        if (categoryNames != null && !categoryNames.isEmpty()) {
            List<Category> categories = categoryNames.stream()
                    .map(NeroUtils::convertToCategory)
                    .collect(Collectors.toList());
            spec = spec.and(StockSpecification.hasAnyCategory(categories));
        }
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceBetween(minPrice.get(), maxPrice.get()));
        } else if (minPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceGreaterThanOrEqualTo(minPrice.get()));
        } else if (maxPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceLessThanOrEqualTo(maxPrice.get()));
        }
        return stockRepository.findAll(spec, pageable).getContent();
    }
}
