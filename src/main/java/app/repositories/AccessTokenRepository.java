package app.repositories;

import app.entity.AccessToken;
import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    AccessToken findByUser(User user);
    AccessToken findByToken(String token);
}