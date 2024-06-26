package guru.qa.niffler.po.pc;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Header {

    private final SelenideElement friendsPageButton = $x("//li[@data-tooltip-id='friends']");
    private final SelenideElement peoplePageButton = $x("//li[@data-tooltip-id='people']");
    private final SelenideElement avatarProfileButton = $("img.header__avatar");

    public void clickFriendsButton() {
        friendsPageButton.click();
    }

    public void clickPeopleButton() {
        peoplePageButton.click();
    }
    public void clickProfileButton() {avatarProfileButton.click();}
}
