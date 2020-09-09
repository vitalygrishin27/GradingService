package app.controller;

import app.entity.Role;
import app.entity.User;
import app.entity.wrapper.AccessTokenWrapper;
import app.service.AccessTokenService;
import app.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getAll(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/roleList")
    public ResponseEntity<List<Role>> getAllRole(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(Arrays.asList(Role.values()), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity save(@NonNull @RequestBody User user, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.saveUserFlow(user)).build();
    }

    @PutMapping("/user")
    public ResponseEntity<AccessTokenWrapper> tryToLogin(@NonNull @RequestBody User user, @RequestAttribute String token) {
        return new ResponseEntity<>(userService.tryToLoginFlow(user), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.deleteUserFlow(id)).build();
    }

}
