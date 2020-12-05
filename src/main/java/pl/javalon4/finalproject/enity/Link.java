package pl.javalon4.finalproject.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "link")
public class Link {

    public Link(String id, String url, String description, LinkStatus status, AppUser user) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    @Id
    private String id;

    private String url;

    private String description;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private LinkCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}
