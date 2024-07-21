package tr.com.nero.stock.barcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.nero.stock.Stock;

@Entity
@Data
@NoArgsConstructor
@Table(name = "barcode")
public class Barcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String barcode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnore
    private Stock stock;

    public Barcode(String bar, Stock stock) {
        this.barcode = bar;
        this.stock = stock;
    }
}
