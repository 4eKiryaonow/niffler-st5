package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.CategorySpendRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;

public class CategoryHibernateExtension extends AbstractCategoryExtension {
    private final CategorySpendRepositoryHibernate categorySpendHibernate = new CategorySpendRepositoryHibernate();

    @Override
    protected CategoryJson createCategory(GenerateCategory category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(category.username());
        categoryEntity.setCategory(category.category());
        categoryEntity = categorySpendHibernate.createCategory(categoryEntity);
        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    protected void removeCategory(CategoryJson categoryJson) {
        categorySpendHibernate.removeCategory(CategoryEntity.fromJson(categoryJson));
    }
}
