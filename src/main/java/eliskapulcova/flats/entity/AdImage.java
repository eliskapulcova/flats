package eliskapulcova.flats.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class AdImage {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public String getImgUrl() {
        return imgUrl;
    }

// getters and setters
}

