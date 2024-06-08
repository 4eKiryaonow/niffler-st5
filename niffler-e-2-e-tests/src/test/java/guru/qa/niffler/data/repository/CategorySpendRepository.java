package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.List;

public interface CategorySpendRepository {

    CategoryEntity createCategory(CategoryEntity categoryEntity);

    CategoryEntity editCategory(CategoryEntity categoryEntity);

    void removeCategory(CategoryEntity categoryEntity);

    SpendEntity createSpend(SpendEntity spendEntity);

    SpendEntity editSpend(SpendEntity spendEntity);

    void removeSpend(SpendEntity spendEntity);
    List<SpendEntity> findAllByUsername(String username);
}
