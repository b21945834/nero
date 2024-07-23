package tr.com.nero.stock;

import jakarta.persistence.*;
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
public class StockDTO {

    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private String title;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Long userId;
    private String brand;
    private BigDecimal discount;
    private BigDecimal cost;
    private BigDecimal vatRate;
    private List<String> barcodes;
}
