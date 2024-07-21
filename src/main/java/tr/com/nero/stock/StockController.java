package tr.com.nero.stock;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tr.com.nero.common.BaseResponse;
import tr.com.nero.user.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
@Tag(name = "Stocks", description = "the Stocks Api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Long>> createStock(@RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock createdStock = stockService.createStock(user, stock);
        return ResponseEntity.ok(new BaseResponse<>(createdStock.getId()));
    }

    @PostMapping("/update")
    public ResponseEntity<BaseResponse> update(@RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock createdStock = stockService.createStock(user, stock);
        return ResponseEntity.ok(new BaseResponse<>(createdStock.getId()));
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<Stock>>> getStocks(
            @RequestParam String category,
            @RequestParam Optional<Double> minPrice,
            @RequestParam Optional<Double> maxPrice,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<String> categories = (category != null) ? Arrays.asList(category.split(",")) : List.of();

        Page<Stock> stocks = stockService.getStocksWithFilters(user.getId(), categories, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(new BaseResponse<>(stocks));
    }
}
