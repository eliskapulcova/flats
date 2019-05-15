package eliskapulcova.flats;

import eliskapulcova.flats.addetail.AdDetailCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"eliskapulcova.flats"})
@EnableAutoConfiguration
//@SpringBootApplication
public class AdDetailRunner implements CommandLineRunner {

    private final AdDetailCrawler adDetailCrawler;

    public static void main(String[] args) {
        SpringApplication.run(AdDetailRunner.class, args);

    }

    @Autowired
    public AdDetailRunner(AdDetailCrawler adDetailCrawler) {
        this.adDetailCrawler = adDetailCrawler;
    }

    public void run(String... args) {
        try {
            this.adDetailCrawler.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            System.exit(1);
        }
    }
}

