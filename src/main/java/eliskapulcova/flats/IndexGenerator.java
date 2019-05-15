package eliskapulcova.flats;

import eliskapulcova.flats.entity.IndexRecord;
import eliskapulcova.flats.repository.IndexRecordRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Random;

@ComponentScan(basePackages = {"eliskapulcova.flats"})
@EnableAutoConfiguration
//@SpringBootApplication
public class IndexGenerator implements CommandLineRunner {

    public IndexRecordRepository indexRecordRepository;

    public static void main(String[] args) {
        SpringApplication.run(IndexGenerator.class, args);
    }

    @Autowired
    public IndexGenerator(IndexRecordRepository indexRecordRepository) {
        this.indexRecordRepository = indexRecordRepository;
    }

    public void run(String... args) throws InterruptedException {
        String chromeDriverPath = "/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--window-size=1920,1200", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

        int currentPage = 1;
        List<WebElement> ads;

        do {
            String urlPart = "https://www.sreality.cz/hledani/pronajem/byty/praha-10,praha-9," +
                    "praha-8,praha-7,praha-6,praha-5,praha-4,praha-3,praha-2,praha-1?velikost=1%2Bkk," +
                    "1%2B1,2%2Bkk,2%2B1,3%2Bkk,3%2B1,4%2Bkk,4%2B1,5%2Bkk,5%2B1,6-a-vice,atypicky&stav=velmi-dobry-stav," +
                    "dobry-stav,spatny-stav,ve-vystavbe,developerske-projekty,novostavby," +
                    "pred-rekonstrukci,po-rekonstrukci&strana=";
            String realityUrl = urlPart + currentPage;
            driver.get(realityUrl);


            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".property")));


            // Search for a.title and get attributes from that element
            ads = ((ChromeDriver) driver).findElementsByCssSelector(".property a.title");

            for (WebElement ad : ads) {
                String adUrl = ad.getAttribute("href");
                System.out.println(adUrl);

                String sRealityId = adUrl.substring(adUrl.lastIndexOf('/') + 1);

                IndexRecord indexRecord = new IndexRecord(sRealityId, adUrl);

                indexRecordRepository.save(indexRecord);
            }

            Thread.sleep(new Random().nextInt(5000) + 5000);
            currentPage++;
        }
        while (!ads.isEmpty());
    }
}






























