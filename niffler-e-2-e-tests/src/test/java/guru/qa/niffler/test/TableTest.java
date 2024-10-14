package guru.qa.niffler.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.SpendCondition;
import guru.qa.niffler.condition.SpendsInTableCondition;
import guru.qa.niffler.condition.UserCondition;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.CategoryHttpExtension;
import guru.qa.niffler.jupiter.extension.SpendHttpExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.po.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.ExecutionException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.UserCondition.usersInTable;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith({CategoryHttpExtension.class, SpendHttpExtension.class, UserQueueExtension.class})
public class TableTest extends BaseTest {

    @GenerateCategory(
            username = "kirill",
            category = "Обучение"
    )

    @Spends({
            @GenerateSpend(
                    username = "kirill",
                    description = "QA.GURU Advanced 5 - обучение",
                    amount = 65000.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение"),

            @GenerateSpend(
                    username = "kirill",
                    description = "QA.GURU Advanced 5 - опыт написания диплома",
                    amount = 2.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение")
    })

    @Test
    void checkTableSpends(SpendJson[] spendJsons) {
        WelcomePage welcomePage = new WelcomePage();
        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginButton();
        loginPage.setUserName("kirill")
                .setPassword("12345")
                .clickSubmitButton();
        ElementsCollection spendings = $(".spendings-table tbody")
                .$$("tr");
        spendings.get(0).scrollIntoView(true);
        spendings.should(SpendCondition.spendsInTable(spendJsons));
    }



    @Test
    void checkTableUsers(@User(WITH_FRIENDS) UserJson user1, @User(WITH_FRIENDS) UserJson anotherUser) {
        WelcomePage welcomePage = new WelcomePage();
        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginButton();
        loginPage.setUserName(user1.username())
                .setPassword(user1.testData().password())
                .clickSubmitButton();
        mainPage.header().clickPeopleButton();
        ElementsCollection users =  $("[class*=abstract-table] tbody").$$("tr");
        UserJson[] usersArray = {user1, anotherUser};
        users.shouldHave(usersInTable(usersArray));
    }
}
