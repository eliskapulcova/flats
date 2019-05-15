package eliskapulcova.flats.scrapping;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class DriverFactory {

    public ChromeDriver create() {
        String chromeDriverPath = "/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--window-size=1920,1200", "--ignore-certificate-errors");
        options.addArguments("--headless", "--window-size=1920,1200", "--ignore-certificate-errors");

        return new ChromeDriver(options);
    }

}
