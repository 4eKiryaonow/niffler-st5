package guru.qa.niffler.po.pc;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleTable {

    private static final String FRIENDSHIP_MESSAGE = "You are friends";
    private static final String PENDING_INVITE_MESSAGE = "Pending invitation";

    private final ElementsCollection rowsPeople = $(".abstract-table").$$("tr");
    private final SelenideElement submitButton = $(".button-icon_type_submit");

    public void invitationReceived() {
        submitButton.shouldBe(visible);
    }

    public void friendshipExists(String username) {
        getRowByUserName(username).shouldHave(text(FRIENDSHIP_MESSAGE));
    }

    public void invitationExists(String username) {
        getRowByUserName(username).shouldHave(text(PENDING_INVITE_MESSAGE));
    }

    private SelenideElement getRowByUserName(String username) {
        return rowsPeople.find(text(username));
    }
}
