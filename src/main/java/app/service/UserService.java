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

import java.util.ArrayList;
import java.util.Arrays;
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

    public List<Role> findRoleDueToken(String token) {
        User user = getUserByToken(token);
        List<Role> roles = new ArrayList<>();
        if (user!=null && (user.getRole().equals(Role.ADMINISTRATOR) ||
                user.getRole().equals(Role.MANAGER))) {
            roles = Arrays.asList(Role.values());
        } else {
            roles.add(Role.PARTICIPANT);
            roles.add(Role.UNDEFINED);
        }
        return roles;
    }

    private User getUserByToken(String token) {
        AccessToken accessToken = accessTokenService.findByToken(token);
        return accessToken != null ? accessToken.getUser() : null;
    }

    public List<User> findAllDueToken(String token) {
        List<User> result = new ArrayList<>();
        User user = getUserByToken(token);
        if (user.getRole().equals(Role.ADMINISTRATOR) ||
                user.getRole().equals(Role.MANAGER) ||
                user.getRole().equals(Role.JURY)) {
            result = findAll();
        } else {
            result.add(user);
        }
        return result;
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
            if (userWithSameLogin != null && userWithSameLogin.getId() != userFromDB.getId()) {
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
        userFromDB.setContests(user.getContests());
        save(userFromDB);
        return HttpStatus.OK;
    }

    public HttpStatus deleteUserFlow(long id) {
        User userFromDB = findById(id);
        // check for existing at least one admin user
        if (userFromDB.getRole() == Role.ADMINISTRATOR && findByRole(Role.ADMINISTRATOR).size() == 1) {
            return HttpStatus.CONFLICT;
        }
        delete(userFromDB);
        return HttpStatus.OK;
    }

    public AccessTokenWrapper tryToLoginFlow(User user) {
        User userFromDB = findByLogin(user.getLogin());
        if (userFromDB != null && isPasswordMatch(user.getPassword(), userFromDB.getEncryptedPassword())) {
            return convertToAccessTokenWrapper(accessTokenService.createAccessToken(userFromDB), userFromDB);
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

