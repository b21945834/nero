package tr.com.nero.common;

import org.springframework.util.StringUtils;
import tr.com.nero.stock.Category;

import java.util.Locale;

public class NeroUtils {
    public static String toUpperCase(String string) {
        return StringUtils.capitalize(string);
    }
    public static Category convertToCategory(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return Category.valueOf(str.trim().toUpperCase(Locale.ENGLISH));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new NeroIllegalArgumentException("Böyle bir kategori bulunamadı: " + str );
        }
    }
}
