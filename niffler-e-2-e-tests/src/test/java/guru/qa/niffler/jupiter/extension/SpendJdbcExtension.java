package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.repository.CategorySpendRepositoryImpl;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;

import java.util.Date;

public class SpendJdbcExtension extends AbstractSpendExtension {

    private final CategorySpendRepositoryImpl spendImpl = new CategorySpendRepositoryImpl();


    @Override
    protected SpendJson createSpend(GenerateSpend spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setSpendDate(new Date());
        spendEntity.setCategory(spend.category());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        spendEntity.setUsername(spend.username());
        return SpendJson.fromEntity(spendImpl.createSpend(spendEntity));
    }

    @Override
    protected void removeSpend(SpendJson spend) {
        spendImpl.removeSpend(SpendEntity.fromJson(spend));
    }
}
