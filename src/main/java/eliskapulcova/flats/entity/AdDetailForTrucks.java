package eliskapulcova.flats.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.jetbrains.annotations.Nullable;

@Entity
public class AdDetailForTrucks {

    @Id
    private String id;

    @Nullable
    @Column(columnDefinition = "TEXT")
    private String name;

    @Nullable
    private String contact;

    protected AdDetailForTrucks() {
    }

    public AdDetailForTrucks(String trucsId) {
        this.id = trucsId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
