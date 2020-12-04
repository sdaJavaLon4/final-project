package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.javalon4.finalproject.enity.AppUser;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    AppUser findByLogin(String login);

    //    this query is No working ->??
//    @Modifying
//    @Query("update app_user u set u.password = :password, u.email = :email where u.login = :login")
//    void setUserByLogin(@Param("password") String password, @Param("email") String email, @Param("login") String login);



}
