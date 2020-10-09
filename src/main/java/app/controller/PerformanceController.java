package app.controller;

import app.entity.Performance;
import app.entity.Role;
import app.entity.User;
import app.service.AccessTokenService;
import app.service.PerformanceService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/performances")
public class PerformanceController {

    @Autowired
    PerformanceService performanceService;
    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping
    public ResponseEntity<List<Performance>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = accessTokenService.findByToken(token).getUser();
        List <Performance> result= new ArrayList<>();
        if (user.getRole().equals(Role.PARTICIPANT)) {
            result = performanceService.findAll().stream().filter(performance -> performance.getUser().equals(user)).collect(Collectors.toList());
        }else{
            result=performanceService.findAll();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Performance> getById(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(performanceService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody Performance performance, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = accessTokenService.findByToken(token).getUser();
        if (user.getRole().equals(Role.PARTICIPANT)) {
            performance.setUser(user);
        }
        return ResponseEntity.status(performanceService.savePerformanceFlow(performance)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(performanceService.deletePerformanceFlow(id)).build();
    }

}
