package guru.qa.niffler.data.constants.query;

public class QueryTableSpend {
    public static final String INSERT = "INSERT INTO %s (username, spend_date, currency, amount, description, category_id) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE %s SET username=?, spend_date=?, currency=?, amount=?, description=?, category_id=? WHERE id=?";
}
