package pl.javalon4.finalproject.enity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "link_category")
@Data
@AllArgsConstructor
public class LinkCategory {


    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Collection<Link> links;

    public LinkCategory() {

    }

    public LinkCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
