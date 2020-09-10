package app.controller;

import app.entity.Contest;
import app.service.AccessTokenService;
import app.service.ContestService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ContestController {

    @Autowired
    ContestService service;

    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping("/contest")
    public ResponseEntity<List<Contest>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping("/contest")
    public ResponseEntity save(@NonNull @RequestBody Contest contest, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.saveFlow(contest)).build();
    }

    @DeleteMapping("/contest/{contestId}")
    public ResponseEntity delete(@PathVariable long contestId, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.deleteFlow(contestId)).build();
    }
}
