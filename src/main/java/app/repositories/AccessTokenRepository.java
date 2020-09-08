package app.repositories;

import app.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    AccessToken findByLogin(String login);
    AccessToken findByToken(String token);
}