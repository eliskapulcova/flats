package eliskapulcova.flats.repository;

import eliskapulcova.flats.entity.IndexRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IndexRecordRepository extends CrudRepository<IndexRecord, UUID> {
    Iterable<IndexRecord> findByScrappedAtIsNull();
}

