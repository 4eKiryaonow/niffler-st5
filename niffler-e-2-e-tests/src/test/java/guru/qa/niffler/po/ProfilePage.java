package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement avatar = $(".avatar-container");
    private final SelenideElement inputNameField = $(byName("firstname"));
    private final SelenideElement inputSurnameField = $(byName("surname"));

    public void checkUserProfile(String username, String name, String surName) {
        avatar.shouldHave(text(username));
        inputNameField.shouldHave(value(name));
        inputSurnameField.shouldHave(value(surName));
    }
}
