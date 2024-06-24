package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    private final SelenideElement toaster = $(".Toastify__toast");

    public T checkMessage(String message) {
        toaster.shouldHave(text(message));
        return (T) this;
    }
}
