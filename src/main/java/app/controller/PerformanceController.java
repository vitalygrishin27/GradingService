package app.controller;

import app.entity.Performance;
import app.entity.Role;
import app.entity.User;
import app.entity.wrapper.AccessTokenWrapper;
import app.service.AccessTokenService;
import app.service.PerformanceService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return new ResponseEntity<>(performanceService.findAll(), HttpStatus.OK);
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
