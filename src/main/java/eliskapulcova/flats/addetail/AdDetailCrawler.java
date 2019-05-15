package eliskapulcova.flats.addetail;

import eliskapulcova.flats.entity.AdDetail;
import eliskapulcova.flats.entity.AdImage;
import eliskapulcova.flats.entity.IndexRecord;
import eliskapulcova.flats.repository.AdDetailRepository;
import eliskapulcova.flats.repository.AdImageRepository;
import eliskapulcova.flats.repository.IndexRecordRepository;
import eliskapulcova.flats.scrapping.DriverFactory;
import eliskapulcova.flats.value.ParamType;
import eliskapulcova.flats.value.PoiType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

//import org.springframework.boot.autoconfigure.SpringBootApplication;

@Component
public class AdDetailCrawler {

    public static final int TIME_OUT_IN_SECONDS = 15;
    private ChromeDriver driver;

    private final IndexRecordRepository indexRecordRepository;

    private final AdDetailRepository adDetailRepository;

    private final AdImageRepository adImageRepository;


    @Autowired
    public AdDetailCrawler(
            DriverFactory driverFactory,
            IndexRecordRepository indexRecordRepository,
            AdDetailRepository adDetailRepository,
            AdImageRepository adImageRepository
    ) {
        this.driver = driverFactory.create();
        this.indexRecordRepository = indexRecordRepository;
        this.adDetailRepository = adDetailRepository;
        this.adImageRepository = adImageRepository;

    }

    public void execute() {
        Iterable<IndexRecord> indexRecords = indexRecordRepository.findByScrappedAtIsNull();
        for (IndexRecord indexRecord : indexRecords) {
            scrapeAdDetail(indexRecord);
            indexRecord.setScrappedAt(new Date());
            indexRecordRepository.save(indexRecord);
        }
        System.exit(0);

    }

    private void scrapeAdDetail(IndexRecord indexRecord) {

        AdDetail adDetail = new AdDetail(indexRecord.getId());

        //System.out.println("-----------starting new ad-------------");

        driver.get(indexRecord.getUrl());

        System.out.println(indexRecord.getUrl());
        new WebDriverWait(driver, TIME_OUT_IN_SECONDS)
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".content-inner")));
        try {
            driver.findElementByCssSelector(".property-title").getText();
        } catch (NoSuchElementException exception) {
            return;
        }
        driver.findElementByCssSelector(".btn.thumbnails").click();
//        driver.findElementByCssSelector(".btn-circle-gallery-fs").click();

        String location = driver.findElementByCssSelector(".property-title .location-text").getText();
        adDetail.setLocation(location);

        String price = driver.findElementByCssSelector(".property-title .norm-price").getText();
        if (price.contains("Info o ceně")) {
            indexRecordRepository.delete(indexRecord);
            return;
        }

        String adjustedNotWholePrice = price.replace(" ", "");

        //System.out.println(adjustedNotWholePrice);
        Matcher matcher = Pattern.compile("(\\d+)").matcher(adjustedNotWholePrice);
        matcher.find();
        String priceMatcher = matcher.group(0);
        //System.out.println(matcher.group(0));
        try {
            Integer pricePerMonth = Integer.valueOf(priceMatcher);
            adDetail.setPricePerMonth(pricePerMonth);
        } catch (NumberFormatException e) {
            return;
        }

        //System.out.println(matcher);


        List<WebElement> descriptionParagraphs = driver.findElementsByCssSelector(
                ".property-detail .description p"
        );
        StringBuilder descriptionBuilder = new StringBuilder();
        for (WebElement paragraph : descriptionParagraphs) {
            if (paragraph.getText().equals(" ")) {
                continue;
            }

            descriptionBuilder
                    .append(paragraph.getText())
                    .append(System.lineSeparator());
        }
        adDetail.setDescription(descriptionBuilder.toString());

        List<WebElement> params = driver.findElementsByCssSelector(".property-detail .params .param");
        //System.out.println(params.size());
        for (WebElement param : params) {
            //System.out.println("step 1");
            String labelValue = param.findElement(By.cssSelector(".param-label")).getText();
            labelValue = labelValue.substring(0, labelValue.length() - 1);

            ParamType paramType;
            try {
                //System.out.println(labelValue);
                paramType = ParamType.fromString(labelValue);

            } catch (IllegalArgumentException e) {
                continue;
            }


            WebElement paramValueElement = param.findElement(By.cssSelector(".param-value"));
            //System.out.println("initiating param processing");
            switch (paramType) {
                case AREA:
                    String rawArea = paramValueElement.getText();
                    //System.out.println(unAdjustedArea);
                    Matcher areaMatcher = Pattern.compile("^(\\d+)").matcher(rawArea);
                    areaMatcher.find();
                    String foundAreaMatch = areaMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer area = Integer.valueOf(foundAreaMatch);
                    adDetail.setArea(area);
//                    } catch (NumberFormatException e) {
//                        return;
//                    }
                    break;
                case FLOOR:
                    Integer floor;
                    String rawFloor = paramValueElement.getText();
                    System.out.println(rawFloor);
                    Matcher floorMatcher = Pattern.compile("^(-?\\d+|přízemí)").matcher(rawFloor);
                    floorMatcher.find();
                    String foundMatch = floorMatcher.group(0);
                    if (foundMatch.equals("přízemí")) {
                        floor = 0;
                    } else {
                        //System.out.println(matcher1.group(0));
                        floor = Integer.valueOf(foundMatch);
                        System.out.println(floor);
//                        try {
//                        } catch (NumberFormatException e) {
//                            return;
//                        }
                    }
                    adDetail.setFloor(floor);
                    break;
                case BALCONY:
                    Boolean hasBalcony = processBoleanishValue(paramValueElement);
                    adDetail.setBalcony(hasBalcony);
                    break;
                case ELEVATOR:
                    Boolean hasElevator = processBoleanishValue(paramValueElement);
                    adDetail.setLift(hasElevator);
                    break;
                case CELLAR:
                    Boolean hasCellar = processBoleanishValue(paramValueElement);
                    adDetail.setCellar(hasCellar);
                    break;
                case GARAGE:
                    Boolean hasGarage = processBoleanishValue(paramValueElement);
                    adDetail.setGarage(hasGarage);
                    break;
                case ACCOMMODATION:
                    String unAdjustedAccommodation = paramValueElement.getText();
                    boolean hasAccommodation;
                    if (unAdjustedAccommodation.equals("částečně")) {
                        hasAccommodation = true;
                    } else {
                        hasAccommodation = processBoleanishValue(paramValueElement);
                    }
                    adDetail.setAccommodation(hasAccommodation);
                    break;
                default:
                    break;
            }
        }

        //System.out.println("---------");


        List<WebElement> paramsNeighborhood = driver.findElementsByCssSelector(".pois li");

        for (WebElement paramNeighborhood : paramsNeighborhood) {

            String labelValue = paramNeighborhood.findElement(By.cssSelector(".poi-label")).getText();
            labelValue = labelValue.substring(0, labelValue.length() - 1);
            System.out.println(labelValue);

            PoiType poiType;
            try {
                poiType = PoiType.fromString(labelValue);

            } catch (IllegalArgumentException e) {
                continue;
            }

            WebElement paramValueElement = paramNeighborhood.findElement(By.cssSelector(".poi-value .distance"));
            //System.out.println("initiating param processing");
            switch (poiType) {
                case UNDERGROUND:
                    String rawUnderground = paramValueElement.getText();
                    Matcher undergroundMatcher = Pattern.compile("(\\d+)").matcher(rawUnderground);
                    undergroundMatcher.find();
                    String foundUndergroundMatch = undergroundMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer underground = Integer.valueOf(foundUndergroundMatch);
                    System.out.println(underground);
                    adDetail.setUnderground(underground);
                    break;
                case TRAM:
                    String rawTram = paramValueElement.getText();
                    Matcher tramMatcher = Pattern.compile("(\\d+)").matcher(rawTram);
                    tramMatcher.find();
                    String foundTramMatch = tramMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer tram = Integer.valueOf(foundTramMatch);
                    System.out.println(tram);
                    adDetail.setTram(tram);
                    break;
                case BUS:
                    String rawBus = paramValueElement.getText();
                    Matcher busMatcher = Pattern.compile("(\\d+)").matcher(rawBus);
                    busMatcher.find();
                    String foundBusMatch = busMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer bus = Integer.valueOf(foundBusMatch);
                    System.out.println(bus);
                    adDetail.setBus(bus);
                    break;
                case TRAIN:
                    String rawTrain = paramValueElement.getText();
                    Matcher trainMatcher = Pattern.compile("(\\d+)").matcher(rawTrain);
                    trainMatcher.find();
                    String foundTrainMatch = trainMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer train = Integer.valueOf(foundTrainMatch);
                    System.out.println(train);
                    adDetail.setTrain(train);
                    break;
                case ATM:
                    String rawAtm = paramValueElement.getText();
                    Matcher atmMatcher = Pattern.compile("(\\d+)").matcher(rawAtm);
                    atmMatcher.find();
                    String foundAtmMatch = atmMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer atm = Integer.valueOf(foundAtmMatch);
                    System.out.println(atm);
                    adDetail.setAtm(atm);
                    break;
                case POST:
                    String rawPost = paramValueElement.getText();
                    Matcher postMatcher = Pattern.compile("(\\d+)").matcher(rawPost);
                    postMatcher.find();
                    String foundPostMatch = postMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer post = Integer.valueOf(foundPostMatch);
                    System.out.println(post);
                    adDetail.setPost(post);
                    break;
                case PHARMACY:
                    String rawPharmacy = paramValueElement.getText();
                    Matcher pharmacyMatcher = Pattern.compile("(\\d+)").matcher(rawPharmacy);
                    pharmacyMatcher.find();
                    String foundPharmacyMatch = pharmacyMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer pharmacy = Integer.valueOf(foundPharmacyMatch);
                    System.out.println(pharmacy);
                    adDetail.setPharmacy(pharmacy);
                    break;
                case SPORT_FIELD:
                    String rawSport = paramValueElement.getText();
                    Matcher sportMatcher = Pattern.compile("(\\d+)").matcher(rawSport);
                    sportMatcher.find();
                    String foundSportMatch = sportMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer sport = Integer.valueOf(foundSportMatch);
                    System.out.println(sport);
                    adDetail.setSport_field(sport);
                    break;
                case RESTAURANT:
                    String rawRest = paramValueElement.getText();
                    Matcher restMatcher = Pattern.compile("(\\d+)").matcher(rawRest);
                    restMatcher.find();
                    String foundRestMatch = restMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer restaurant = Integer.valueOf(foundRestMatch);
                    System.out.println(restaurant);
                    adDetail.setRestaurant(restaurant);
                    break;
                case SHOP:
                    String rawShop = paramValueElement.getText();
                    Matcher shopMatcher = Pattern.compile("(\\d+)").matcher(rawShop);
                    shopMatcher.find();
                    String foundShopMatch = shopMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer shop = Integer.valueOf(foundShopMatch);
                    System.out.println(shop);
                    adDetail.setShop(shop);
                    break;
                case SCHOOL:
                    String rawSchool = paramValueElement.getText();
                    Matcher schoolMatcher = Pattern.compile("(\\d+)").matcher(rawSchool);
                    schoolMatcher.find();
                    String foundSchoolMatch = schoolMatcher.group(0);
                    //System.out.println(matcher1.group(0));
//                    try {
                    Integer school = Integer.valueOf(foundSchoolMatch);
                    System.out.println(school);
                    adDetail.setSchool(school);
                    break;
                default:
                    break;
            }
        }
        adDetailRepository.save(adDetail);
        processImages(adDetail);
    }


    private void processImages(AdDetail adDetail) {

        List<WebElement> imageElements = driver.findElementsByCssSelector(".thumbnails-inner .list .thumb-img");
        System.out.println(imageElements.size());
        //System.out.println("-------------");

        List<AdImage> currentAdImages = adImageRepository.findByAdDetail(adDetail);
        for (AdImage adImage : currentAdImages) {
            adImageRepository.delete(adImage);
        }

        for (WebElement imageElement : imageElements) {
            //System.out.println(imageElement);

            new WebDriverWait(driver, TIME_OUT_IN_SECONDS)
                    .until(ExpectedConditions.attributeToBeNotEmpty(imageElement, "src"));

            String unAdjustedImageUrl = imageElement.getAttribute("src");
            System.out.println(unAdjustedImageUrl);
            Matcher imageMatcher = Pattern.compile("(^.*\\.(?:jpeg|png|mpo|webp)\\?fl=)").matcher(unAdjustedImageUrl);
            imageMatcher.find();
            StringBuilder imageUrlBuilder = new StringBuilder();
            String foundImageMatch = imageMatcher.group(0);


            String imageUrlParameters = "res,1920,1080,1|wrm,/watermark/sreality.png,10|shr,,20|jpg,90";

            imageUrlBuilder
                    .append(foundImageMatch)
                    .append(imageUrlParameters);

            //System.out.println(imageUrlBuilder.toString());
            AdImage adImage = new AdImage(adDetail, imageUrlBuilder.toString());

            adImageRepository.save(adImage);

            //System.out.println(unAdjustedImageUrl);
        }
    }


    //try {
    // AdDetail adDetail;
    // } catch (NullPointerException exception) {
    //    return;
    //}
    // System.out.println("--------------------");

    private boolean processBoleanishValue(WebElement paramValueElement) {
        //System.out.println("step 3");
        if (!paramValueElement.getText().isEmpty()) {
            return true;
        }

        try {
            paramValueElement.findElement(By.cssSelector(".icon-ok"));
            return true;
        } catch (NoSuchElementException exception) {
            paramValueElement.findElement(By.cssSelector(".icon-cross"));
            return false;
        }
    }


//       System.out.println(ad);
//       System.out.println(location);

//       System.out.println(descriptionParagraphs);


}


