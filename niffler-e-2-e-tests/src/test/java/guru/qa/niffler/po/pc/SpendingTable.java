package guru.qa.niffler.po.pc;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable {

    private final ElementsCollection rowsWithSpending = $(".spendings-table tbody").$$("tr");
    private final By cellSelector = By.cssSelector("td");


    private SelenideElement getRowByDescription(String description) {
        return rowsWithSpending.find(text(description));
    }

    private SelenideElement getCheckBox(String description) {
        return getRowByDescription(description).$$(cellSelector).first();
    }

    public void clickCheckBoxByDescription(String description) {
        getCheckBox(description).scrollIntoView(true).click();
    }

    public void checkSizeTable(int size) {
        rowsWithSpending.shouldHave(size(size));
    }


}
