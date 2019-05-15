package eliskapulcova.flats.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class IndexRecord {

    @Id
    String id;

    String url;

    Date scrappedAt;

    protected IndexRecord() {
    }

    public IndexRecord(String sRealityId, String adUrl) {
        this.id = sRealityId;
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
