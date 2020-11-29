package pl.javalon4.finalproject.enity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

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
}
