package app.controller;

import app.entity.bom.CriterionBom;
import app.entity.converter.CriterionConverter;
import app.service.AccessTokenService;
import app.service.CriterionService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/criteria")
public class CriterionController {

    @Autowired
    CriterionService criterionService;
    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    CriterionConverter criterionConverter;

    @GetMapping
    public ResponseEntity<List<CriterionBom>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(criterionConverter.toBom(criterionService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriterionBom> getById(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(criterionConverter.toBom(criterionService.findById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody CriterionBom criterionBom, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(criterionService.saveCriterionFlow(criterionConverter.fromBom(criterionBom))).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(criterionService.deleteCriterionFlow(id)).build();
    }

}
