package eliskapulcova.flats.addetail;

import eliskapulcova.flats.entity.AdDetailForTrucks;
import eliskapulcova.flats.entity.IndexRecordForTrucs;
import eliskapulcova.flats.repository.AdDetailTrucksRepository;
import eliskapulcova.flats.repository.IndexRecordForTrucsRepository;
import eliskapulcova.flats.scrapping.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class AdDetailTrucksCrawler {

    public static final int TIME_OUT_IN_SECONDS = 50;
    private final IndexRecordForTrucsRepository indexRecordForTrucsRepository;
    private final AdDetailTrucksRepository adDetailTrucksRepository;
    private DriverFactory driverFactory;
    private RemoteWebDriver driver;

    @Autowired
    public AdDetailTrucksCrawler(
            DriverFactory driverFactory,
            IndexRecordForTrucsRepository indexRecordForTrucsRepository,
            AdDetailTrucksRepository adDetailTrucksRepository
    ) {
        this.driverFactory = driverFactory;
        this.indexRecordForTrucsRepository = indexRecordForTrucsRepository;
        this.adDetailTrucksRepository = adDetailTrucksRepository;
    }

    private RemoteWebDriver getDriver() {
        if (driver == null) {
            driver = driverFactory.create();
        }

        return driver;
    }

    public void execute() {
        Iterable<IndexRecordForTrucs> indexRecords = indexRecordForTrucsRepository.findByScrappedAtIsNull();
        for (IndexRecordForTrucs indexRecord : indexRecords) {
            scrapeAdDetail(indexRecord);
            indexRecord.setScrappedAt(new Date());
            indexRecordForTrucsRepository.save(indexRecord);
        }
        System.exit(0);

    }

    private void scrapeAdDetail(IndexRecordForTrucs indexRecordForTrucs) {

        AdDetailForTrucks adDetail = new AdDetailForTrucks(indexRecordForTrucs.getId());

        getDriver().get(indexRecordForTrucs.getUrl());

        System.out.println(indexRecordForTrucs.getUrl());
        new WebDriverWait(driver, TIME_OUT_IN_SECONDS)
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".container.foodtrucktop")));
        try {
            String name = getDriver().findElementByCssSelector(".nombre-ft").getText();
            System.out.println(name);
        } catch (NoSuchElementException exception) {
            return;
        }
    }
}
