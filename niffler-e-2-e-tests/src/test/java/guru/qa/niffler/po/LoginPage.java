package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");

    public void inputLogin(String login) {
        loginField.setValue(login);
    }

    public void inputPassword(String password) {
            passwordField.setValue(password);
    }

    public void clickSubmitButton() {
            submitButton.click();
    }
}
