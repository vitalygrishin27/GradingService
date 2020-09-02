package app.controller;

import app.entity.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
    @PostMapping("/user")
    public ResponseEntity save(@ModelAttribute User user) {
        return ResponseEntity.status(userService.saveUserFlow(user)).build();
    }
    @PutMapping("/user")
    public ResponseEntity update(@ModelAttribute User user) {
        return ResponseEntity.status(userService.saveUserFlow(user)).build();
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        return ResponseEntity.status(userService.deleteUserFlow(id)).build();
    }
}
