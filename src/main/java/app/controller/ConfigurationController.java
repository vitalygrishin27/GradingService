package app.controller;

import app.entity.Configuration;
import app.service.ConfigurationService;
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

    @GetMapping("/configuration")
    public ResponseEntity<List<Configuration>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
    @PostMapping("/configuration")
    public ResponseEntity save(@ModelAttribute Configuration configuration) {
        return ResponseEntity.status(service.saveFlow(configuration)).build();
    }
    @PutMapping("/configuration")
    public ResponseEntity update(@ModelAttribute Configuration configuration) {
        return ResponseEntity.status(service.saveFlow(configuration)).build();
    }
    @DeleteMapping("/configuration/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        return ResponseEntity.status(service.deleteFlow(id)).build();
    }
}
