package app.controller;

import app.entity.bom.CategoryBom;
import app.entity.converter.CategoryConverter;
import app.service.AccessTokenService;
import app.service.CategoryService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    CategoryConverter categoryConverter;

    @GetMapping
    public ResponseEntity<List<CategoryBom>> getAll(@RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(categoryConverter.toBom(categoryService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryBom> getById(@RequestAttribute String token, @PathVariable long id) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(categoryConverter.toBom(categoryService.findById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity save(@NonNull @RequestBody CategoryBom categoryBom, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(categoryService.saveCategoryFlow(categoryConverter.fromBom(categoryBom), false)).build();
    }

    @PostMapping("/onlyName")
    public ResponseEntity saveOnlyName(@NonNull @RequestBody CategoryBom categoryBom, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(categoryService.saveCategoryFlow(categoryConverter.fromBom(categoryBom), true)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id, @RequestAttribute String token) {
        if (!accessTokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(categoryService.deleteCategoryFlow(id)).build();
    }

}
