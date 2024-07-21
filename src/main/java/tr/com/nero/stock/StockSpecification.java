package tr.com.nero.stock;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class StockSpecification {

    public static Specification<Stock> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Stock> hasAnyCategory(List<Category> categories) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Category> inClause = criteriaBuilder.in(root.get("category"));
            for (Category category : categories) {
                inClause.value(category);
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
