package eliskapulcova.flats.repository;

import eliskapulcova.flats.entity.AdDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdDetailRepository extends CrudRepository<AdDetail, String> {
    Optional<AdDetail> findFirstByApartmentRatingIsNull();
}
