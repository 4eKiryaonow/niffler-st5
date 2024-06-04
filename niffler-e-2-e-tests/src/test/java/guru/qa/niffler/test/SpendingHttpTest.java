package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryHttpExtension;
import guru.qa.niffler.jupiter.extension.SpendHttpExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import guru.qa.niffler.po.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith({CategoryHttpExtension.class, SpendHttpExtension.class})
public class SpendingHttpTest {

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
        WelcomePage welcomePage = new WelcomePage();
        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginButton();
        loginPage.setUserName("dima")
                .setPassword("12345")
                .clickSubmitButton();
        mainPage.getSpendingTable()
                .clickCheckBoxByDescription(spendJson.description())
                .clickDeleteSpending();
        mainPage.getSpendingTable().checkSizeTable(0);
    }
}
