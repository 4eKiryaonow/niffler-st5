package guru.qa.niffler.data.constants.query;

public class QueryTableCategory {

    public static final String INSERT = "INSERT INTO %s (category, username) values (?,?)";
    public static final String UPDATE = "UPDATE %s SET category=?, username=? WHERE id = ?";

    public static final String SELECT_BY_NAME = "SELECT * FROM %s WHERE NAME = ?";
}
