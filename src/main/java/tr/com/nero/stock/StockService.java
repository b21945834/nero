package tr.com.nero.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.nero.stock.category.CategoryService;
import tr.com.nero.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final CategoryService categoryService;
    private final StockRepository stockRepository;

    @Transactional
    public Stock createStock(User user, StockRequest stock) {
        //TODO VALIDATE STOCK
        Stock newStock = Stock.builder()
                .price(stock.getPrice())
                .barcodes(stock.getBarcodes())
                .user(user)
                .quantity(stock.getQuantity())
                .category(categoryService.getCategoryByName(stock.getCategory()))
                .build();
        return stockRepository.save(newStock);
    }

    @Transactional
    public Stock update(User user, StockRequest stock) {
        //TODO VALIDATE STOCK
        Stock newStock = Stock.builder()
                .price(stock.getPrice())
                .barcodes(stock.getBarcodes())
                .user(user)
                .quantity(stock.getQuantity())
                .category(categoryService.getCategoryByName(stock.getCategory()))
                .build();
        return stockRepository.save(newStock);
    }

    public List<Stock> getWithUserId(Long userId) {
        return stockRepository.findByUserId(userId);
    }

    public Optional<Stock> getWithId(Long id, Long userId) {
        return stockRepository.findByIdAndUserId(id, userId);
    }

    public Page<Stock> getStocksWithFilters(Long userId, List<String> categories, Optional<Double> minPrice, Optional<Double> maxPrice, Pageable pageable) {
        Specification<Stock> spec = Specification.where(StockSpecification.hasUserId(userId));

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and(StockSpecification.hasAnyCategory(categories));
        }
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceBetween(minPrice.get(), maxPrice.get()));
        } else if (minPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceGreaterThanOrEqualTo(minPrice.get()));
        } else if (maxPrice.isPresent()) {
            spec = spec.and(StockSpecification.hasPriceLessThanOrEqualTo(maxPrice.get()));
        }

        return stockRepository.findAll(spec, pageable);
    }
}
