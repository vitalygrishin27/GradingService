package app.controller;

import app.entity.Role;
import app.entity.User;
import app.entity.bom.UserBom;
import app.entity.converter.UserConverter;
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
    @Autowired
    UserConverter userConverter;

    @GetMapping
    public ResponseEntity<List<UserBom>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userConverter.toBom(userService.findAllDueToken(token)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserBom> getById(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userConverter.toBom(userService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/roleList")
    public ResponseEntity<List<Role>> getAllRole(@RequestAttribute String token) {
        return new ResponseEntity<>(userService.findRoleDueToken(token), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody UserBom userBom, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token) &&
                (userBom.getRole().equals(Role.ADMINISTRATOR) ||
                        userBom.getRole().equals(Role.MANAGER)
                        || userBom.getRole().equals(Role.JURY))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.saveUserFlow(userConverter.fromBom(userBom))).build();
    }

    @PutMapping
    public ResponseEntity<AccessTokenWrapper> tryToLogin(@NonNull @RequestBody UserBom userBom, @RequestAttribute String token) {
        return new ResponseEntity<>(userService.tryToLoginFlow(userConverter.fromBom(userBom)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(userService.deleteUserFlow(id)).build();
    }

}
