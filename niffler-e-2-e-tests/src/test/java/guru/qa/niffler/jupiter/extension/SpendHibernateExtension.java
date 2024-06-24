package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.CategorySpendRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;

import java.util.Date;

public class SpendHibernateExtension extends AbstractSpendExtension {
    private final CategorySpendRepositoryHibernate categorySpendRepositoryHibernate =
            new CategorySpendRepositoryHibernate();

    @Override
    protected SpendJson createSpend(GenerateSpend spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setSpendDate(new Date());
        spendEntity.setCategory(new CategoryEntity(null, spend.category(), spend.username()));
        spendEntity.setCurrency(spend.currency());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        spendEntity.setUsername(spend.username());
        return SpendJson.fromEntity(categorySpendRepositoryHibernate.createSpend(spendEntity));
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        categorySpendRepositoryHibernate.removeSpend(SpendEntity.fromJson(spend));
    }
}
