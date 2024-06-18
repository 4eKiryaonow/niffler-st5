package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement avatar = $(".avatar-container");
    private final SelenideElement inputNameField = $(byName("firstname"));
    private final SelenideElement inputSurnameField = $(byName("surname"));

    public void checkUserProfile(UserJson user) {
        checkUserName(user.username());
        checkName(user.firstname());
        checkSurname(user.surname());
    }

    private void checkUserName(String username) {
        avatar.shouldHave(text(username));
    }

    private void checkName(String name) {
        inputNameField.shouldHave(value(name));
    }

    private void checkSurname(String surName) {
        inputSurnameField.shouldHave(value(surName));
    }
}
