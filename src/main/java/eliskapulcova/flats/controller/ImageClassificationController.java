package eliskapulcova.flats.controller;

import eliskapulcova.flats.controller.model.RateAdDetail;
import eliskapulcova.flats.entity.AdDetail;
import eliskapulcova.flats.entity.AdImage;
import eliskapulcova.flats.repository.AdDetailRepository;
import eliskapulcova.flats.repository.AdImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
public class ImageClassificationController {

    private final AdImageRepository adImageRepository;
    private final AdDetailRepository adDetailRepository;

    public ImageClassificationController(AdImageRepository adImageRepository, AdDetailRepository adDetailRepository) {
        this.adImageRepository = adImageRepository;
        this.adDetailRepository = adDetailRepository;
    }

    @GetMapping("/image-rating")
    public String renderImage(Model model) {
        Optional<AdDetail> maybeAdDetail = adDetailRepository.findFirstByApartmentRatingIsNull();
        if (!maybeAdDetail.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        AdDetail adDetail = maybeAdDetail.get();

        List<AdImage> images = adImageRepository.findByAdDetail(adDetail);

        final RateAdDetail adDetailRating = new RateAdDetail();
        adDetailRating.setAdDetailId(adDetail.getId());

        model.addAttribute("model", adDetailRating);
        model.addAttribute("adDetail", adDetail);
        model.addAttribute("images", images);

        return "image-rating";
    }

    @PostMapping("/image-rating")
    public RedirectView editFormSubmit(
        @RequestParam("adDetailId") String adDetailId,
        @RequestParam("apartmentRating") Integer apartmentRating,
        @RequestParam("photographersSkillRating") Integer photographersSkillRating
    ) {
        Optional<AdDetail> MaybeAdDetail = adDetailRepository.findById(adDetailId);
        if (!MaybeAdDetail.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        AdDetail adDetail = MaybeAdDetail.get();
        adDetail.updateRatings(apartmentRating, photographersSkillRating);
        this.adDetailRepository.save(adDetail);

        return new RedirectView("/image-rating");
    }

    @PostMapping("/image-rating-delete")
    public RedirectView deleteFormSubmit(@RequestParam("adDetailId") String adDetailId) {
        Optional<AdDetail> MaybeAdDetail = adDetailRepository.findById(adDetailId);
        if (!MaybeAdDetail.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        AdDetail adDetail = MaybeAdDetail.get();
        this.adDetailRepository.delete(adDetail);

        return new RedirectView("/image-rating");
    }
}
