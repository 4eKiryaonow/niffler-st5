package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;

public class CategoryHttpExtension extends AbstractCategoryExtension {

    private static final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    protected CategoryJson createCategory(GenerateCategory category) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                category.category(),
                category.username()
        );
        try {
            return spendApiClient.createCategory(categoryJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeCategory(CategoryJson category) {
    }
}