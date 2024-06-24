package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;

import java.util.Date;

public class SpendHttpExtension extends AbstractSpendExtension {

    private final SpendApiClient spendApiClient = new SpendApiClient();


    @Override
    protected SpendJson createSpend(GenerateSpend spend) {
        SpendJson spendJson = new SpendJson(
                null,
                new Date(),
                spend.category(),
                spend.currency(),
                spend.amount(),
                spend.description(),
                spend.username()
        );
        try {
            return spendApiClient.createSpend(spendJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeSpend(SpendJson spend) {
    }
}