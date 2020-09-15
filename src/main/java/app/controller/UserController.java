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

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping
    public ResponseEntity<List<User>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.findAllDueToken(token), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/roleList")
    public ResponseEntity<List<Role>> getAllRole(@RequestAttribute String token) {
        return new ResponseEntity<>(userService.findRoleDueToken(token), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody User user, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token) &&
                (user.getRole().equals(Role.ADMINISTRATOR) ||
                        user.getRole().equals(Role.MANAGER)
                        || user.getRole().equals(Role.JURY))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.saveUserFlow(user)).build();
    }

    @PutMapping
    public ResponseEntity<AccessTokenWrapper> tryToLogin(@NonNull @RequestBody User user, @RequestAttribute String token) {
        return new ResponseEntity<>(userService.tryToLoginFlow(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.deleteUserFlow(id)).build();
    }

}
