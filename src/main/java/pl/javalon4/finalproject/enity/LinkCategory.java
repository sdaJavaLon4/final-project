package pl.javalon4.finalproject.enity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "link_category")
@Data
public class LinkCategory {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Collection<Link> links;
}
