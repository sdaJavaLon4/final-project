package pl.javalon4.finalproject.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "app_user")
public class AppUser {

    @Id
    private String id;

    private String login;

    private String password;

    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<LinkCategory> linkCategories;
}
