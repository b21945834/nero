package tr.com.nero.common;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tr.com.nero.stock.barcode.Barcode;
import tr.com.nero.stock.Stock;
import tr.com.nero.stock.StockDTO;
import tr.com.nero.user.User;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface NeroMapper {
    NeroMapper INSTANCE = Mappers.getMapper(NeroMapper.class);

    StockDTO fromStockEntity(Stock stock);

    default List<String> map(List<Barcode> value) {
        List<String> newL = new ArrayList<>();
        for(Barcode barcode : value) {
            newL.add(barcode.getBarcode());
        }
        return newL;
    }

    default Long map(User user) {
        return user.getId();
    }
}
