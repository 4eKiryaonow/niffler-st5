package guru.qa.niffler.data;

import guru.qa.niffler.config.Config;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataBase {
    AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
    CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
    SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
    USER_DATA("jdbc:postgresql://%s:%d/niffler-userdata");

    private final String JdbcURL;
    private final static Config CFG = Config.getInstance();

    public String getJdbcURL() {
        return String.format(JdbcURL, CFG.dbHost(), CFG.dbPort());
    }

}
