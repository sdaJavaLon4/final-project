package pl.javalon4.finalproject.enity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "link")
@ToString(exclude = {"category", "user"})
public class Link {

    @Id
    private String id;

    private String url;

    private String description;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private LinkCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}
