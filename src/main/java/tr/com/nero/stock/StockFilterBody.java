package tr.com.nero.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockFilterBody implements Cloneable {
    @Schema(description = "Stok kategorisi", example = "Elektronics")
    private String category;

    @Schema(description = "Stok için minimum fiyat", example = "100.0")
    private Double minPrice;

    @Schema(description = "Stok için maksimum fiyat", example = "500.0")
    private Double maxPrice;

    @Schema(description = "Sayfalandırma için sayfa boyutu", example = "10")
    private int size = 10;

    @Schema(description = "Sayfalandırma için sayfa numarası", example = "0")
    private int page = 0;

    @Schema(hidden = true)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockFilterBody that = (StockFilterBody) o;
        return size == that.size &&
                page == that.page &&
                Objects.equals(category, that.category) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(maxPrice, that.maxPrice) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, category, minPrice, maxPrice, size, page);
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
