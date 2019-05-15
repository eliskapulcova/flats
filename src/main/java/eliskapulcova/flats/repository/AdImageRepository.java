package eliskapulcova.flats.repository;

import eliskapulcova.flats.entity.AdDetail;
import eliskapulcova.flats.entity.AdImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AdImageRepository extends CrudRepository<AdImage, UUID> {
    List<AdImage> findByAdDetail(AdDetail adDetail);
}
