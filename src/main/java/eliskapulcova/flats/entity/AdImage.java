package eliskapulcova.flats.entity;

import javax.persistence.*;
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

    private Integer rating;

    protected AdImage() {}

    public AdImage(AdDetail adDetail, String imgUrl) {
        this.id = UUID.randomUUID();
        this.adDetail = adDetail;
        this.imgUrl = imgUrl;
    }





    // getters and setters
}

