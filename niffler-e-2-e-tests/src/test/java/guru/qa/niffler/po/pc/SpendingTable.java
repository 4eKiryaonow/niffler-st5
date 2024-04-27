package guru.qa.niffler.po.pc;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable {

    private final ElementsCollection rowsWithSpending = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSpendingButton = $(".spendings__bulk-actions button");
    private final By cellSelector = By.cssSelector("td");

    public void clickDeleteSpending() {
        deleteSpendingButton.click();
    }

    public SpendingTable clickCheckBoxByDescription(String description) {
        getCheckBox(description).scrollIntoView(true).click();
        return this;
    }

    public void checkSizeTable(int size) {
        rowsWithSpending.shouldHave(size(size));
    }

    private SelenideElement getRowByDescription(String description) {
        return rowsWithSpending.find(text(description));
    }

    private SelenideElement getCheckBox(String description) {
        return getRowByDescription(description).$$(cellSelector).first();
    }
}
