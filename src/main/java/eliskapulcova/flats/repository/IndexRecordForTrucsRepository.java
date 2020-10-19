package eliskapulcova.flats.repository;

import eliskapulcova.flats.entity.IndexRecordForTrucs;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface IndexRecordForTrucsRepository extends CrudRepository<IndexRecordForTrucs, UUID> {
    Iterable<IndexRecordForTrucs> findByScrappedAtIsNull();
}
