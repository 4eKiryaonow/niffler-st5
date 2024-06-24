package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategorySpendRepositoryHibernate implements CategorySpendRepository {
    private final EntityManager entityManager = EmProvider.entityManager(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        entityManager.persist(categoryEntity);
        return categoryEntity;

    }

    @Override
    public CategoryEntity editCategory(CategoryEntity categoryEntity) {
        return entityManager.merge(categoryEntity);
    }

    @Override
    public void removeCategory(CategoryEntity categoryEntity) {
        entityManager.remove(categoryEntity);

    }

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        entityManager.persist(spendEntity);
        return spendEntity;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spendEntity) {
        return entityManager.merge(spendEntity);
    }

    @Override
    public void removeSpend(SpendEntity spendEntity) {
        entityManager.remove(spendEntity);

    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        String hql = "SELECT * FROM spend WHERE username = :username";
        return entityManager.createQuery(hql, SpendEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public CategoryEntity findCategory(String category, String username) {
        String hql = "SELECT * FROM category WHERE username = :username AND category = :category";
        return entityManager.createQuery(hql, CategoryEntity.class)
                .setParameter("username", username)
                .setParameter("category", category)
                .getSingleResult();
    }
}
