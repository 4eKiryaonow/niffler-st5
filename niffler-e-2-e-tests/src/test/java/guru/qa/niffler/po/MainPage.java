package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.po.pc.SpendingTable;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement loginButton = $("a[href*='redirect']");
    private final SelenideElement deleteSpendingButton = $(".spendings__bulk-actions button");
    private SpendingTable spendingTable;

    public MainPage() {
        spendingTable = new SpendingTable();
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void clickDeleteSpending() {
        deleteSpendingButton.click();
    }

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }
}
