package tr.com.nero.common;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tr.com.nero.stock.Barcode;
import tr.com.nero.stock.BarcodeDTO;
import tr.com.nero.stock.Stock;
import tr.com.nero.stock.StockDTO;

@Mapper
public interface NeroMapper {
    NeroMapper INSTANCE = Mappers.getMapper(NeroMapper.class);

    StockDTO fromStockEntity(Stock stock);
    Stock toStockEntity(StockDTO stockDTO);

    BarcodeDTO fromBarcodeEntity(Barcode barcode);
    Barcode toBarcodeEntity(BarcodeDTO barcodeDTO);


}
