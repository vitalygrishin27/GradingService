package app.service;

import app.entity.AccessToken;
import app.entity.Role;
import app.entity.User;
import app.entity.wrapper.AccessTokenWrapper;
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

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public HttpStatus saveUserFlow(User user) {
        User userFromDB = findById(user.getId());

        if (user.getId() != -1) {
            //Check login
            if (userFromDB == null) {
                return HttpStatus.CONFLICT;
            }
            User userWithSameLogin = findByLogin(user.getLogin());
            if (userWithSameLogin.getId() != userFromDB.getId()) {
                return HttpStatus.CONFLICT;
            }
            //check for new password
            if (user.getPassword() != null) {
                user.setEncryptedPassword(encryptePassword(user.getPassword()));
            }
            // check for existing at least one admin user
            if (userFromDB.getRole() == Role.ADMINISTRATOR && user.getRole() != Role.ADMINISTRATOR) {
                if (findByRole(Role.ADMINISTRATOR).size() == 1) {
                    return HttpStatus.CONFLICT;
                }
            }
        } else {
            userFromDB = findByLogin(user.getLogin());
            //check for unique login
            if (userFromDB != null) {
                return HttpStatus.CONFLICT;
            }
            userFromDB = new User();
        }
        userFromDB.setLogin(user.getLogin());
        if (user.getId() == -1) {
            userFromDB.setEncryptedPassword(encryptePassword(user.getPassword()));
        } else if (user.getPassword() != null) {
            userFromDB.setEncryptedPassword(user.getEncryptedPassword());
        }
        userFromDB.setPassword(user.getPassword());
        userFromDB.setRole(user.getRole());
        userFromDB.setPhoto(user.getPhoto());
        userFromDB.setPosition(user.getPosition());
        userFromDB.setLastName(user.getLastName());
        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setSecondName(user.getSecondName());
        save(userFromDB);
        return HttpStatus.OK;
    }

    public HttpStatus deleteUserFlow(long id) {
        delete(findById(id));
        return HttpStatus.OK;
    }

    public AccessTokenWrapper tryToLoginFlow(User user) {
        User userFromDB = findByLogin(user.getLogin());
        if (userFromDB != null && isPasswordMatch(user.getPassword(), userFromDB.getEncryptedPassword())) {
            return convertToAccessTokenWrapper(accessTokenService.createAccessToken(userFromDB.getLogin()), userFromDB);
        } else {
            return null;
        }
    }

    private AccessTokenWrapper convertToAccessTokenWrapper(AccessToken accessToken, User user) {
        AccessTokenWrapper accessTokenWrapper = new AccessTokenWrapper();
        accessTokenWrapper.setLogin(user.getLogin());
        accessTokenWrapper.setToken(accessToken.getToken());
        accessTokenWrapper.setRole(user.getRole());
        return accessTokenWrapper;
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

