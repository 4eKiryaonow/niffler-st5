package guru.qa.niffler.po.pc;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class ReactCalendar extends BaseComponent<ReactCalendar> {

    private final SelenideElement dateInput = self.$(".react-datepicker__input-container input");

    public ReactCalendar(SelenideElement self) {
        super(self);
    }

    public ReactCalendar() {
        super($(".calendar-wrapper"));
    }

    public ReactCalendar selectDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(date);
        dateInput.clear();
        dateInput.setValue(formattedDate);
        dateInput.sendKeys(Keys.ENTER);
        self.shouldNot(Condition.exist);
        return this;
    }
}
