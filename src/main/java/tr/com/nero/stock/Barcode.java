package tr.com.nero.stock;

import jakarta.persistence.*;
@Entity
@Table(name = "barcode")
public class Barcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String barcode;
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
}
