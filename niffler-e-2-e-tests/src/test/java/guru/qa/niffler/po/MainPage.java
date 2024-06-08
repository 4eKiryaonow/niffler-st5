package guru.qa.niffler.po;

import guru.qa.niffler.po.pc.Header;
import guru.qa.niffler.po.pc.SpendingTable;

public class MainPage {

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
}
