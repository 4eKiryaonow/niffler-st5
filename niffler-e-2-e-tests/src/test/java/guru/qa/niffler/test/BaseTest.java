package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.config.Config;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {

    protected static final Config CFG = Config.getInstance();

    static {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "start-maximized");
        Configuration.browserCapabilities = options;
    }
}
