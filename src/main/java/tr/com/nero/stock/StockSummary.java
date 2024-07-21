package tr.com.nero.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockSummary {
    private Long id;
    private Double price;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Category category;
}
