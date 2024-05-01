package guru.qa.niffler.po;

import guru.qa.niffler.po.pc.PeopleTable;

public class FriendsPage {
    private final PeopleTable peopleTable = new PeopleTable();

    public PeopleTable table() {
        return peopleTable;
    }
}
