package tr.com.nero.stock;

import java.util.List;

public class StockDTO {

    private Long id;
    private Double price;
    private Integer quantity;
    private String category;
    private Long userId;
    private List<BarcodeDTO> barcodes;
}
