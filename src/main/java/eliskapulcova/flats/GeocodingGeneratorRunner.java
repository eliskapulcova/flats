package eliskapulcova.flats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"eliskapulcova.flats"})
@EnableAutoConfiguration
//@SpringBootApplication
public class GeocodingGeneratorRunner implements CommandLineRunner {

    private final GeocodingGenerator geocodingGenerator;

    @Autowired
    public GeocodingGeneratorRunner(GeocodingGenerator geocodingGenerator) {

        this.geocodingGenerator = geocodingGenerator;
    }

    public static void main(String[] args) {
        SpringApplication.run(GeocodingGeneratorRunner.class, args);

    }

    public void run(String... args) {
        try {
            this.geocodingGenerator.run();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            System.exit(1);
        }
    }
}

