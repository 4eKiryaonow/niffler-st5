package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.CategorySpendRepositoryImpl;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {

    private final CategorySpendRepositoryImpl spendImpl = new CategorySpendRepositoryImpl();

    @Override
    protected CategoryJson createCategory(GenerateCategory category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(category.username());
        categoryEntity.setCategory(category.category());
        categoryEntity = spendImpl.createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson categoryJson) {
        spendImpl.removeCategory(CategoryEntity.fromJson(categoryJson));
    }
}
