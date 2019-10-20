package eliskapulcova.flats.repository;

import eliskapulcova.flats.entity.AdDetail;
import org.springframework.data.repository.CrudRepository;

public interface AdDetailRepository extends CrudRepository<AdDetail, String> {
    AdDetail findOneByApartmentRatingIsNull();
}
