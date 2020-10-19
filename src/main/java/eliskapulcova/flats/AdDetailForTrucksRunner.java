package eliskapulcova.flats;

import eliskapulcova.flats.addetail.AdDetailTrucksCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"eliskapulcova.flats"})
@EnableAutoConfiguration
public class AdDetailForTrucksRunner implements CommandLineRunner {

    private final AdDetailTrucksCrawler adDetailTrucksCrawler;

    @Autowired
    public AdDetailForTrucksRunner(AdDetailTrucksCrawler adDetailTrucksCrawler) {
        this.adDetailTrucksCrawler = adDetailTrucksCrawler;
    }

    public static void main(String[] args) {
        SpringApplication.run(eliskapulcova.flats.AdDetailForTrucksRunner.class, args);

    }

    public void run(String... args) {
        try {
            this.adDetailTrucksCrawler.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            System.exit(1);
        }
    }
}
