package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        String env = System.getProperty("test.env", "local");

        return switch (env) {
            case "local" -> LocalConfig.instance;
            case "docker" -> DockerConfig.instance;
            default -> throw new IllegalStateException("Can not find config");
        };
    }

    String frontUrl();

    String spendUrl();

    String dbHost();
    String gatewayUrl();

    default int dbPort() {
        return 5432;
    }
}
