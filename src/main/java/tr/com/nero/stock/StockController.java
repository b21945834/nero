package tr.com.nero.stock;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tr.com.nero.common.BaseResponse;
import tr.com.nero.common.NeroMapper;
import tr.com.nero.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stock")
@Tag(name = "Stocks", description = "the Stocks Api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> createStock(@Valid @RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock createdStock = stockService.createStock(user, stock);
        return ResponseEntity.ok(new BaseResponse<>(createdStock.getId()));
    }

    @PostMapping("/update")
    public ResponseEntity<BaseResponse<StockDTO>> update(@RequestParam("id") Long id, @Valid @RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock createdStock = stockService.update(user, id, stock);
        return ResponseEntity.ok(new BaseResponse<>(NeroMapper.INSTANCE.fromStockEntity(createdStock)));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        stockService.delete(user.getId(), id);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<StockDTO>>> getStocks(
            @RequestParam Optional<String> category,
            @RequestParam Optional<Double> minPrice,
            @RequestParam Optional<Double> maxPrice,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        StockFilterBody filters = StockFilterBody.builder().page(page).size(size).category(category.orElse(null)).minPrice(minPrice.orElse(null)).maxPrice(maxPrice.orElse(null)).userId(user.getId()).build();
        String currentPageKey = "stocks:" + filters.hashCode();

        // Check if the current page's data is in the cache
        List<StockDTO> cachedStocks = (List<StockDTO>) redisTemplate.opsForValue().get(currentPageKey);
        if (cachedStocks != null) {
            return ResponseEntity.ok(new BaseResponse<>(cachedStocks));
        }

        List<Stock> stocks = stockService.getStocksWithFilters(filters);
        List<StockDTO> dtos = new ArrayList<>();
        stocks.forEach(stock -> dtos.add(NeroMapper.INSTANCE.fromStockEntity(stock)));

        cacheNextPage(filters);

        return ResponseEntity.ok(new BaseResponse<>(dtos));
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<BaseResponse<Stock>> getStockDetail(@PathVariable("stockId") Long stockId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<Stock> stock = stockService.getWithId(user.getId(), stockId);
        return stock.map(value -> ResponseEntity.ok(new BaseResponse<>(value))).orElseGet(() -> ResponseEntity.ok(new BaseResponse<>("Hata: Stock bulunamadı.")));
    }
    private void cacheNextPage(StockFilterBody filters) {
        try {
            StockFilterBody nextPageFilters = (StockFilterBody) filters.clone();
            nextPageFilters.setPage(filters.getPage() + 1);

            List<Stock> nextPageStocks = stockService.getStocksWithFilters(nextPageFilters);
            List<StockDTO> nextPageDtos = new ArrayList<>();
            nextPageStocks.forEach(stock -> nextPageDtos.add(NeroMapper.INSTANCE.fromStockEntity(stock)));

            // Create a key for the next page filters
            String key = "stocks:" + nextPageFilters.hashCode();
            redisTemplate.opsForValue().set(key, nextPageDtos, 2, TimeUnit.MINUTES);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
