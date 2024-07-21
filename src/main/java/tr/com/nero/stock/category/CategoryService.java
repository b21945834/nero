package tr.com.nero.stock.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            return createCategory(name);
        }
        return category;
    }

    public Long getCategoryIdByName(String name) {
        Category category = categoryRepository.findByName(name);
        return (category != null) ? category.getId() : null;
    }

    public Category createCategory(String name) {
        Category newCategory = Category.builder()
                .name(name)
                .build();
        return categoryRepository.save(newCategory);
    }
}
