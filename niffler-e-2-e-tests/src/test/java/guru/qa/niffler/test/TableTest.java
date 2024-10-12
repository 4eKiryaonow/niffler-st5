package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.extension.CategoryHttpExtension;
import guru.qa.niffler.jupiter.extension.SpendHttpExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import guru.qa.niffler.po.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CategoryHttpExtension.class, SpendHttpExtension.class})
public class TableTest extends BaseTest {

    @GenerateCategory(
            username = "dima",
            category = "Обучение"
    )

    @Spends({
            @GenerateSpend(
                    username = "dima",
                    description = "QA.GURU Advanced 5 - обучение",
                    amount = 65000.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение"),

            @GenerateSpend(
                    username = "dima",
                    description = "QA.GURU Advanced 5 - опыт написания диплома",
                    amount = 2.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение")
    })

    @Test
    void checkTableRows(SpendJson[] spendJsons) {
        WelcomePage welcomePage = new WelcomePage();
        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginButton();
        loginPage.setUserName("dima")
                .setPassword("12345")
                .clickSubmitButton();
        mainPage.getSpendingTable().checkSizeTable(spendJsons.length);
    }


}
