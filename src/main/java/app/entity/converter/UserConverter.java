package app.entity.converter;

import app.entity.*;
import app.entity.bom.CriterionBom;
import app.entity.bom.UserBom;
import app.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    @Autowired
    UserService userService;

    @Autowired
    ContestConverter contestConverter;

    public User fromBom(UserBom bom) {
        User result = new User();
        if (bom.getId() != null && bom.getId() != -1) {
            result = userService.findById(bom.getId());
        }
        result.setLogin(bom.getLogin());
        result.setPassword(bom.getPassword());
        if (StringUtils.isNotEmpty(bom.getPassword())) result.setEncryptedPassword(UserService.encryptePassword(bom.getPassword()));
        result.setFirstName(bom.getFirstName());
        result.setSecondName(bom.getSecondName());
        result.setLastName(bom.getLastName());
        result.setRole(bom.getRole());
        result.setContests(contestConverter.fromBom(bom.getContests()));

        return result;
    }

    public UserBom toBom(User user) {
        UserBom result = new UserBom();
        result.setId(user.getId());
        result.setLogin(user.getLogin());
        result.setPassword(user.getPassword());
        result.setFirstName(user.getFirstName());
        result.setSecondName(user.getSecondName());
        result.setLastName(user.getLastName());
        result.setRole(user.getRole());
        result.setContests(contestConverter.toBom(user.getContests()));
        return result;
    }

    public List<User> fromBom(List<UserBom> boms) {
        List<User> result = new ArrayList<>();
        for (UserBom bom : boms
        ) {
            result.add(fromBom(bom));
        }
        return result;
    }

    public List<UserBom> toBom(List<User> source) {
        List<UserBom> result = new ArrayList<>();
        for (User user : source
        ) {
            result.add(toBom(user));
        }
        return result;
    }
}
