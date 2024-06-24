package guru.qa.niffler.po;

import guru.qa.niffler.po.pc.Header;
import guru.qa.niffler.po.pc.ReactCalendar;
import guru.qa.niffler.po.pc.SpendingTable;

public class MainPage extends BasePage<MainPage> {

    private final ReactCalendar calendar = new ReactCalendar();

    private final SpendingTable spendingTable = new SpendingTable();
    private final Header header = new Header();

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

    public Header header() {
        return header;
    }

    public void clickAvatar() {
        header().clickProfileButton();
    }
    public ReactCalendar calendar() {
        return this.calendar;
    }
}
