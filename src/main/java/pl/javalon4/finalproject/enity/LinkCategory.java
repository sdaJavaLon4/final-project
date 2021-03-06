package pl.javalon4.finalproject.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity(name = "link_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "links"})
public class LinkCategory {

    public LinkCategory(String id, String name, AppUser user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Link> links;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}
