package tr.com.nero.stock;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import tr.com.nero.stock.category.CategoryService;

import java.util.List;

public class StockSpecification {

    @Setter
    private static CategoryService categoryService;

    public static Specification<Stock> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Stock> hasAnyCategory(List<String> categoryNames) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("category").get("id"));
            for (String name : categoryNames) {
                Long categoryId = categoryService.getCategoryIdByName(name);
                if (categoryId != null) {
                    inClause.value(categoryId);
                }
            }
            return inClause;
        };
    }

    public static Specification<Stock> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<Stock> hasPriceGreaterThanOrEqualTo(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Stock> hasPriceLessThanOrEqualTo(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
