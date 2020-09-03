package app.service;

import app.entity.User;
import app.repositories.CRUDInterface;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements CRUDInterface<User> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
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

    public HttpStatus saveUserFlow(User user){
        return HttpStatus.OK;
    }

    public HttpStatus deleteUserFlow(long id){
        delete(findById(id));
        return HttpStatus.OK;
    }

}
