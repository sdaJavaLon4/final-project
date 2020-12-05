package pl.javalon4.finalproject.enity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "link")
public class Link {

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
