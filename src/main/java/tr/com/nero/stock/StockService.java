package tr.com.nero.stock;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.nero.common.NeroUtils;
import tr.com.nero.stock.barcode.Barcode;
import tr.com.nero.stock.barcode.BarcodeService;
import tr.com.nero.user.User;

import java.util.ArrayList;
import java.util.Arrays;
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
                .discount(stock.getDiscount())
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

    public List<Stock> getStocksWithFilters(StockFilterBody body) {
        return getStocksWithFilters(body.getUserId(),
                body.getCategory(), body.getMinPrice(), body.getMaxPrice(), PageRequest.of(body.getPage(), body.getSize(), Sort.by("id").ascending()));
    }

    public List<Stock> getStocksWithFilters(Long userId, String category, Double minPrice, Double maxPrice, Pageable pageable) {
        Specification<Stock> spec = Specification.where(StockSpecification.hasUserId(userId));
        List<String> categoryNames = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            categoryNames = Arrays.stream(category.split(",")).toList();
        }
        if (categoryNames != null && !categoryNames.isEmpty()) {
            List<Category> categories = categoryNames.stream()
                    .map(NeroUtils::convertToCategory)
                    .collect(Collectors.toList());
            spec = spec.and(StockSpecification.hasAnyCategory(categories));
        }
        if (minPrice != null && maxPrice != null) {
            spec = spec.and(StockSpecification.hasPriceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(StockSpecification.hasPriceGreaterThanOrEqualTo(minPrice));
        } else if (maxPrice != null) {
            spec = spec.and(StockSpecification.hasPriceLessThanOrEqualTo(maxPrice));
        }
        return stockRepository.findAll(spec, pageable).getContent();
    }

    public void updateStock(Long id, Long stockId, Integer quantity) {
        Optional<Stock> optionalStock = stockRepository.findDetailsById(id, stockId);
        if (optionalStock.isEmpty()) {
            throw new RuntimeException("Böyle bir stok bulunamadı: " + stockId);
        }
        Stock stock = optionalStock.get();
        stock.setQuantity(quantity);
        stockRepository.save(stock);
    }
}
