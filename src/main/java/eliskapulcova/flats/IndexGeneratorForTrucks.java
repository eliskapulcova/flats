package eliskapulcova.flats;

import eliskapulcova.flats.entity.IndexRecordForTrucs;
import eliskapulcova.flats.repository.IndexRecordForTrucsRepository;
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
public class IndexGeneratorForTrucks implements CommandLineRunner {

    public IndexRecordForTrucsRepository indexRecordForTrucsRepository;

    @Autowired
    public IndexGeneratorForTrucks(IndexRecordForTrucsRepository indexRecordForTrucsRepository) {
        this.indexRecordForTrucsRepository = indexRecordForTrucsRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(IndexGeneratorForTrucks.class, args);
    }

    public void run(String... args) throws InterruptedException {
        String chromeDriverPath = "/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized", "--ignore-certificate-errors");
//        options.addArguments("--headless", "--window-size=1920,1200", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

        int currentPage = 1;
        List<WebElement> ads;

        do {
            String urlPart = "http://www.foodtruckya.com/foodtrucks/localizador";
            // String realityUrl = urlPart + currentPage;
            driver.get(urlPart);

            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".container")));

            // Search for a.title and get attributes from that element
            ads = ((ChromeDriver) driver).findElementsByCssSelector(".mosaic-block.bar2 .mosaic-overlay");
            //System.out.println(ads);

            for (WebElement ad : ads) {
                System.out.println(ad);
                String adUrl = ad.getAttribute("href");
                System.out.println(adUrl);

                String truckId = adUrl.substring(adUrl.lastIndexOf('/') + 1);

                IndexRecordForTrucs indexRecordForTrucs = new IndexRecordForTrucs(truckId, adUrl);

                indexRecordForTrucsRepository.save(indexRecordForTrucs);
            }

            Thread.sleep(new Random().nextInt(5000) + 5000);
            currentPage++;
        }
        while (!ads.isEmpty());
    }
}
