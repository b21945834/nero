package tr.com.nero.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum Category {
    ELECTRONICS,
    APPAREL,
    FOOD,
    TOYS,
    BOOKS,
    HOME;
}