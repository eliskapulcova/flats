package eliskapulcova.flats.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class IndexRecordForTrucs {

    @Id
    String id;

    String url;

    Date scrappedAt;

    protected IndexRecordForTrucs() {
    }

    public IndexRecordForTrucs(String trucsId, String adUrl) {
        this.id = trucsId;
        this.url = adUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setScrappedAt(Date scrappedAt) {
        this.scrappedAt = scrappedAt;
    }
}
