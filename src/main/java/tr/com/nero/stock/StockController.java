package tr.com.nero.stock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tr.com.nero.common.BaseResponse;
import tr.com.nero.common.NeroMapper;
import tr.com.nero.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/stock")
@Tag(name = "Stocks", description = "the Stocks Api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/add")
    @Operation(
            summary = "Yeni bir stok oluşturur",
            description = "Yeni bir stok oluşturur ve stokun ID'sini döner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla stok oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz stok bilgileri")
    })
    public ResponseEntity<BaseResponse<Long>> createStock(
            @Parameter(description = "Oluşturulacak stok bilgilerini içeren JSON nesnesi", required = true)
            @Valid @RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock createdStock = stockService.createStock(user, stock);
        return ResponseEntity.ok(new BaseResponse<>(createdStock.getId()));
    }

    @PostMapping("/update")
    @Operation(
            summary = "Var olan bir stoku günceller",
            description = "Stok bilgilerini günceller ve güncellenmiş stok bilgilerini döner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla stok güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz stok bilgileri"),
            @ApiResponse(responseCode = "404", description = "Stok bulunamadı")
    })
    public ResponseEntity<BaseResponse<StockDTO>> update(
            @Parameter(description = "Güncellenecek stokun ID'si", required = true)
            @RequestParam("id") Long id,
            @Parameter(description = "Güncellenmiş stok bilgilerini içeren nesnesi", required = true)
            @Valid @RequestBody StockRequest stock) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Stock updatedStock = stockService.update(user, id, stock);
        return ResponseEntity.ok(new BaseResponse<>(NeroMapper.INSTANCE.fromStockEntity(updatedStock)));
    }

    @GetMapping("/delete/{id}")
    @Operation(
            summary = "Belirtilen ID'ye sahip stoku siler",
            description = "Stoku ID'ye göre siler."
    )
    public ResponseEntity<BaseResponse<Void>> delete(
            @Parameter(description = "Silinecek stokun ID'si", required = true)
            @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        stockService.delete(user.getId(), id);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    @GetMapping("/list")
    @Operation(
            summary = "Stokları filtreler ve listeler",
            description = "Filtreler ve stokları döner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla stoklar listelendi")
    })
    public ResponseEntity<BaseResponse<List<StockDTO>>> getStocks(
            @Parameter(description = "Stokları filtrelemek için kategori")
            @RequestParam Optional<String> category,
            @Parameter(description = "Minimum fiyat")
            @RequestParam Optional<Double> minPrice,
            @Parameter(description = "Maksimum fiyat")
            @RequestParam Optional<Double> maxPrice,
            @Parameter(description = "Sayfadaki stok sayısı", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Getirilecek sayfa numarası", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        StockFilterBody filters = StockFilterBody.builder()
                .page(page)
                .size(size)
                .category(category.orElse(null))
                .minPrice(minPrice.orElse(null))
                .maxPrice(maxPrice.orElse(null))
                .userId(user.getId())
                .build();
        String currentPageKey = "stocks:" + filters.hashCode();
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

    @PostMapping("/{stockId}")
    @Operation(
            summary = "Stok miktarını günceller",
            description = "Belirtilen stok ID'sinin miktarını günceller."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla stok miktarı güncellendi"),
            @ApiResponse(responseCode = "404", description = "Stok bulunamadı")
    })
    public ResponseEntity<BaseResponse<Void>> updateStock(
            @Parameter(description = "Güncellenecek stokun ID'si", required = true)
            @PathVariable("stockId") Long stockId,
            @Parameter(description = "Yeni stok miktarı", required = true)
            @RequestParam Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        stockService.updateStock(user.getId(), stockId, quantity);
        return ResponseEntity.ok(new BaseResponse<>());
    }

    @GetMapping("/{stockId}")
    @Operation(
            summary = "Stok detaylarını getirir",
            description = "Belirtilen stok ID'sine sahip stokun detaylarını döner."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla stok detayları döner"),
            @ApiResponse(responseCode = "404", description = "Stok bulunamadı")
    })
    public ResponseEntity<BaseResponse<Stock>> getStockDetail(
            @Parameter(description = "Detayları getirilecek stokun ID'si", required = true)
            @PathVariable("stockId") Long stockId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<Stock> stock = stockService.getWithId(user.getId(), stockId);
        return stock.map(value -> ResponseEntity.ok(new BaseResponse<>(value)))
                .orElseGet(() -> ResponseEntity.ok(new BaseResponse<>("Hata: Stok bulunamadı.")));
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
