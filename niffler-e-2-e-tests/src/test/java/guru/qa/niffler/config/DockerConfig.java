package guru.qa.niffler.config;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    private DockerConfig() {}
    @Override
    public String frontUrl() {
        return "http:/frontend.niffler.dc/";
    }

    @Override
    public String spendUrl() {
        return "http:/frontend.niffler.dc:8093/";
    }

    @Override
    public String dbHost() {
        return "niffler-all-db";
    }

    @Override
    public String gatewayUrl() {
        return "http:/gateway.niffler.dc:8090/";
    }
}
