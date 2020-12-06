package pl.javalon4.finalproject.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity(name = "link_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkCategory {

    public LinkCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Link> links;
}
