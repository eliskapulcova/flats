package eliskapulcova.flats.entity;


//import org.graalvm.compiler.replacements.IntrinsicGraphBuilder;

import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdDetail {

    @Id
    private String id;

    @Nullable
    @Column(columnDefinition="TEXT")
    private String description;

    @Nullable
    private Integer pricePerMonth;

    @Nullable
    private Integer floor;

    @Nullable
    private Integer area;


    @Nullable
    private String location;

    private Boolean balcony;

    /* Boolean terrace; */

    private Boolean cellar;

    private Boolean parkPlace;

    private Boolean garage;

    private Boolean accommodation;

    private Boolean lift;


    private Integer underground;

    private Integer tram;

    private Integer bus;

    private Integer train;
    private Integer atm;

    private Integer post;

    private Integer pharmacy;

    private Integer sport_field;

    private Integer restaurant;

    private Integer shop;

    private Integer school;

    private Integer photographersSkillRating;

    private Integer apartmentRating;

    protected AdDetail() {
    }

    public AdDetail(String sRealityId) {
        this.id = sRealityId;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPricePerMonth(Integer pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBalcony(Boolean hasBalcony) {
        this.balcony = hasBalcony;
    }

//    public void setTerrace(Boolean terrace) {
//////        this.terrace = terrace;
//////    }

    public void setCellar(Boolean hasCellar) {
        this.cellar = hasCellar;
    }

//    public void setParkPlace(Boolean parkPlace) {
//        this.parkPlace = parkPlace;
//    }

    public void setGarage(Boolean hasGarage) {
        this.garage = hasGarage;
    }

    public void setAccommodation(Boolean hasAccommodation) {
        this.accommodation = hasAccommodation;
    }

    public void setLift(Boolean hasElevator) {
        this.lift = hasElevator;
    }

    public void setUnderground(Integer underground) {
        this.underground = underground;
    }

    public void setTram(Integer tram) {
        this.tram = tram;
    }

    public void setBus(Integer bus) {
        this.bus = bus;
    }

    public void setTrain(Integer train) {
        this.train = train;
    }

    public void setAtm(Integer atm) {
        this.atm = atm;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public void setPharmacy(Integer pharmacy) {
        this.pharmacy = pharmacy;
    }

    public void setSport_field(Integer sport_field) {
        this.sport_field = sport_field;
    }

    public void setRestaurant(Integer restaurant) {
        this.restaurant = restaurant;
    }

    public void setShop(Integer shop) {
        this.shop = shop;
    }

    public void setSchool(Integer school) {
        this.school = school;
    }

    public void updateRatings(Integer photographersSkillRating, Integer apartmentRating) {
        this.photographersSkillRating = photographersSkillRating;
        this.apartmentRating = apartmentRating;
    }
}
