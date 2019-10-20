package eliskapulcova.flats.controller.model;

public class AdDetailRating {
    private String adDetailId;
    private Integer apartmentRating;
    private Integer photographersSkillRating;

    public String getAdDetailId() {
        return adDetailId;
    }

    public void setAdDetailId(String adDetailId) {
        this.adDetailId = adDetailId;
    }

    public Integer getApartmentRating() {
        return apartmentRating;
    }

    public void setApartmentRating(Integer apartmentRating) {
        this.apartmentRating = apartmentRating;
    }

    public Integer getPhotographersSkillRating() {
        return photographersSkillRating;
    }

    public void setPhotographersSkillRating(Integer photographersSkillRating) {
        this.photographersSkillRating = photographersSkillRating;
    }
}
