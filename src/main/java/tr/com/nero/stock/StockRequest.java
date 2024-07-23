package tr.com.nero.stock;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {
    @NotNull(message = "Fiyat boş olamaz.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fiyat 0'dan büyük olmalıdır.")
    private BigDecimal price;

    @NotNull(message = "Miktar boş olamaz.")
    @Min(value = 1, message = "Miktar en az 1 olmalıdır.")
    private Integer quantity;

    @NotBlank(message = "Kategori boş olamaz.")
    private String category;

    @NotBlank(message = "Stok ismi boş olamaz.")
    private String title;

    @NotEmpty(message = "Barkodlar boş olamaz.")
    private List<@NotBlank(message = "Barkod boş olamaz.") String> barcodes;

    @NotBlank(message = "Marka boş olamaz.")
    private String brand;

    @DecimalMin(value = "0.0", inclusive = true, message = "İndirim 0 veya daha büyük olmalıdır.")
    private BigDecimal discount;

    @NotNull(message = "Maliyet boş olamaz.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maliyet 0'dan büyük olmalıdır.")
    private BigDecimal cost;

    @Digits(integer = 3, fraction = 2, message = "KDV oranı en fazla 3 tam sayı ve 2 ondalık basamak içermelidir")
    @DecimalMin(value = "0.0", inclusive = true, message = "KDV oranı 0 veya daha büyük olmalıdır")
    @DecimalMax(value = "100.0", inclusive = true, message = "KDV oranı 100 veya daha küçük olmalıdır")
    private BigDecimal vatRate;
}
