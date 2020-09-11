package app.service;

import app.entity.AccessToken;
import app.entity.User;
import app.repositories.AccessTokenRepository;
import app.repositories.CRUDInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccessTokenService implements CRUDInterface<AccessToken> {

    @Autowired
    AccessTokenRepository repository;

    @Override
    public void save(AccessToken accessToken) {
        repository.save(accessToken);
    }

    @Override
    public AccessToken findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AccessToken> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(AccessToken accessToken) {
        repository.delete(accessToken);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public AccessToken findByUser(User user) {
        return repository.findByUser(user);
    }

    public AccessToken findByToken(String token) {
        return repository.findByToken(token);
    }

    public AccessToken createAccessToken(User user) {
        AccessToken accessToken = new AccessToken();
        accessToken.setDateFrom(LocalDateTime.now());
        accessToken.setDateEnd(LocalDateTime.now().plusDays(1));
        accessToken.setUser(user);
        accessToken.setToken(UUID.randomUUID().toString().toUpperCase());
        repository.save(accessToken);
        return accessToken;
    }

    public boolean isTokenValid(String token) {
        if (token.equals("null")) {
            return false;
        }
        AccessToken accessToken = findByToken(token);
        if (accessToken == null) {
            return false;
        }
        return accessToken.getDateEnd().isAfter(LocalDateTime.now());
    }
}
