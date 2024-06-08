package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import guru.qa.niffler.po.ProfilePage;
import guru.qa.niffler.po.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith(DbCreateUserExtension.class)
public class UserTest {

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "start-maximized");
        Configuration.browserCapabilities = options;
    }

    @TestUser
    @Test
    void userLoginTest(UserJson user) {
        WelcomePage welcomePage = new WelcomePage();
        MainPage mainPage = new MainPage();
        LoginPage loginPage = new LoginPage();
        ProfilePage profilePage = new ProfilePage();
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginButton();
        loginPage.setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSubmitButton();
        mainPage.clickAvatar();
        profilePage.checkUserProfile(user.username(), user.firstname(), user.surname());
    }
}
