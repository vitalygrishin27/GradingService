package app.controller;

import app.entity.bom.ContestBom;
import app.entity.converter.ContestConverter;
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
@RequestMapping("/contests")
public class ContestController {

    @Autowired
    ContestService service;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    ContestConverter contestConverter;

    @GetMapping
    public ResponseEntity<List<ContestBom>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(contestConverter.toBom(service.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestBom> getAll(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(contestConverter.toBom(service.findById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody ContestBom contestBom, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.saveFlow(contestConverter.fromBom(contestBom))).build();
    }

    @DeleteMapping("/{contestId}")
    public ResponseEntity delete(@PathVariable long contestId, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.deleteFlow(contestId)).build();
    }
}
