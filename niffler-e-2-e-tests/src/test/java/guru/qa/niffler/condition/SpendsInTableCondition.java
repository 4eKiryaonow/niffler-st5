package guru.qa.niffler.condition;


import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.DateUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class SpendsInTableCondition extends WebElementsCondition {

    private final SpendJson[] expectedSpends;

    public SpendsInTableCondition(SpendJson[] expectedSpends) {
        this.expectedSpends = expectedSpends;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
            return CheckResult.rejected(
                    "Spending table size mismatch",
                    elements.size()
            );
        }

        for (int i = 0; i < elements.size(); i++) {
            WebElement row = elements.get(i);
            SpendJson expectedSpend = expectedSpends[i];
            List<WebElement> fields = row.findElements(By.cssSelector("td"));

            boolean dateResult = fields.get(1).getText().contains(
                    DateUtils.formatDate(
                            expectedSpend.spendDate(),
                            "dd MMM yy"
                    )
            );

            if (!dateResult) {
                return CheckResult.rejected(
                        "Spending table: date mismatch",
                        fields.get(1).getText()
                );
            }

            boolean amountResult = Double.valueOf(fields.get(2).getText()).equals(expectedSpend.amount());

            if (!amountResult) {
                return CheckResult.rejected(
                        "Spending table: amount mismatch",
                        fields.get(2).getText()
                );
            }

            boolean currencyResult = fields.get(3).getText().contains(expectedSpend.currency().name());

            if (!currencyResult) {
                return CheckResult.rejected(
                        "Spending table: currency mismatch",
                        fields.get(3).getText()
                );
            }

            boolean categoryResult = fields.get(4).getText().contains(expectedSpend.category());

            if (!categoryResult) {
                return CheckResult.rejected(
                        "Spending table: category missmatch",
                        fields.get(4).getText()
                );
            }

            boolean descriptionResult = fields.get(5).getText().contains(expectedSpend.description());

            if (!descriptionResult) {
                return CheckResult.rejected(
                        "Spending table: description missmatch",
                        fields.get(5).getText()
                );
            }

        }
        return CheckResult.accepted();
    }

    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        StringBuilder actualElementStringBuilder = new StringBuilder();
        List<WebElement> elements = collection.getElements();
        for (WebElement element : elements) {
            List<WebElement> fields = element.findElements(By.cssSelector("td"));
            String formattedRow = String.format("- %s | %s | %s | %s | %s",
                    fields.get(1).getText(),
                    fields.get(2).getText(),
                    fields.get(3).getText(),
                    fields.get(4).getText(),
                    fields.get(5).getText()
            );
            actualElementStringBuilder.append(formattedRow).append("\n");
        }

        String actualElementString = actualElementStringBuilder.toString();
        StringBuilder expectedElementStringBuilder = new StringBuilder();
        for (SpendJson expectedSpend : expectedSpends) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#.##", symbols);
            String formattedExpected = String.format("- %s | %s | %s | %s | %s",
                    DateUtils.formatDate(expectedSpend.spendDate(), "dd MMM yy"),
                    formatter.format(expectedSpend.amount()),
                    expectedSpend.currency().name(),
                    expectedSpend.category(),
                    expectedSpend.description()
            );
            expectedElementStringBuilder.append(formattedExpected).append("\n");
        }

        String expectedElementString = expectedElementStringBuilder.toString();


        String message = lastCheckResult.getMessageOrElse(() -> "Spending mismatch");
        throw new SpendMismatchException(message, collection, expectedElementString, actualElementString, explanation, timeoutMs, cause);
    }

    @Override
    public String toString() {
        return "";
    }
}

