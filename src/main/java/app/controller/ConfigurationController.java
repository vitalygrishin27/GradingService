package app.controller;

import app.entity.Configuration;
import app.entity.wrapper.ConfigurationWrapper;
import app.service.AccessTokenService;
import app.service.ConfigurationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ConfigurationController {

    @Autowired
    ConfigurationService service;

    @Autowired
    AccessTokenService accessTokenService;

    @GetMapping("/configuration")
    public ResponseEntity<List<Configuration>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping("/configuration")
    public ResponseEntity save(@NonNull @RequestBody ConfigurationWrapper configurationWrapper, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.saveFlow(configurationWrapper.getConfigurations())).build();
    }

    @DeleteMapping("/configuration/{configKey}")
    public ResponseEntity delete(@PathVariable String configKey, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(service.deleteFlow(configKey)).build();
    }
}
