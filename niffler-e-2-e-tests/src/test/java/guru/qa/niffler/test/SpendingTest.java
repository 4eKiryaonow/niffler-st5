package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith(SpendExtension.class)
@ExtendWith(CategoryExtension.class)
public class SpendingTest {

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "start-maximized");
        Configuration.browserCapabilities = options;
    }

    @GenerateCategory(
            username = "dima",
            category = "Обучение"
    )

    @GenerateSpend(
            username = "dima",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        Selenide.open("http://127.0.0.1:3000/");
        MainPage mainPage = new MainPage();
        mainPage.clickLoginButton();
        LoginPage loginPage = new LoginPage();
        loginPage.inputLogin("dima");
        loginPage.inputPassword("12345");
        loginPage.clickSubmitButton();
        mainPage.getSpendingTable().clickCheckBoxByDescription(spendJson.description());
        mainPage.clickDeleteSpending();
        mainPage.getSpendingTable().checkSizeTable(0);
    }
}
