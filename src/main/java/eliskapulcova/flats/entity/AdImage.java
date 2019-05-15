package eliskapulcova.flats.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
public class AdImage {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="ad_detail_id", nullable=false)
    private AdDetail adDetail;

    private String imgUrl;

    protected AdImage() {}

    public AdImage(AdDetail adDetail, String imgUrl) {
        this.id = UUID.randomUUID();
        this.adDetail = adDetail;
        this.imgUrl = imgUrl;
    }





    // getters and setters
}

