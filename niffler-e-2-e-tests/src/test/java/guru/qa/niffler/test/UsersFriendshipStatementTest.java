package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.po.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

@ExtendWith(UserQueueExtension.class)
public class UsersFriendshipStatementTest extends BaseTest {

    WelcomePage welcomePage = new WelcomePage();
    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();
    FriendsPage friendsPage = new FriendsPage();
    PeoplePage peoplePage = new PeoplePage();

    @Test
    void userHasFriend(@User(WITH_FRIENDS) UserJson user1, @User(WITH_FRIENDS) UserJson anotherUser) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginButton();
        loginPage.setUserName(user1.username())
                .setPassword(user1.testData().password())
                .clickSubmitButton();
        mainPage.header().clickFriendsButton();
        friendsPage.table().friendshipExists(anotherUser.username());
    }

    @Test
    void userHasInvitation(@User(INVITATION_RECEIVED) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginButton();
        loginPage.setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSubmitButton();
        mainPage.header().clickFriendsButton();
        friendsPage.table().invitationReceived();
    }

    @Test
    void userSentInvitation(@User(INVITATION_SEND) UserJson user1, @User(INVITATION_SEND) UserJson user2) {
        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginButton();
        loginPage.setUserName(user1.username())
                .setPassword(user1.testData().password())
                .clickSubmitButton();
        mainPage.header().clickPeopleButton();
        peoplePage.table().invitationExists(user2.username());
    }
}
