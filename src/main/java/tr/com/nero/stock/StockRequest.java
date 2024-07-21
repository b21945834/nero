package tr.com.nero.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.nero.user.User;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {
    private Double price;
    private Integer quantity;
    private String category;
    private List<Barcode> barcodes;
}
