package app.service;

import app.entity.AccessToken;
import app.entity.User;
import app.repositories.CRUDInterface;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements CRUDInterface<User> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessTokenService accessTokenService;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public HttpStatus saveUserFlow(User user) {
        return HttpStatus.OK;
    }

    public HttpStatus deleteUserFlow(long id) {
        delete(findById(id));
        return HttpStatus.OK;
    }

    public AccessToken tryToLoginFlow(User user) {
        User userFromDB = findByLogin(user.getLogin());
        if (userFromDB != null && isPasswordMatch(user.getPassword(), userFromDB.getEncryptedPassword())) {
            return accessTokenService.createAccessToken(userFromDB.getLogin());
        } else {
            return null;
        }
    }

    private static String encryptePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private static Boolean isPasswordMatch(String password, String encryptedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, encryptedPassword);
    }
}

