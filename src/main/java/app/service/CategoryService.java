package app.service;

import app.entity.Category;
import app.entity.Criterion;
import app.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements CRUDInterface<Category> {

    @Autowired
    CategoryRepository repository;

    @Autowired
    CriterionService criterionService;

    @Override
    public void save(Category category) {
        repository.save(category);
    }

    @Override
    public Category findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Category category) {
        repository.delete(category);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public Category findByName(String name) {
        return repository.findByName(name);
    }

    public HttpStatus saveCategoryFlow(Category category, boolean updateOnlyName) {
        Category categoryFromDB;
        if (category.getId() == -1) {
            //check for unique name
            if (findByName(category.getName()) != null) {
                return HttpStatus.CONFLICT;
            }
            categoryFromDB = new Category();
        } else {
            categoryFromDB = findById(category.getId());
            if (!category.getName().equals(categoryFromDB.getName())) {
                //check for unique name
                if (findByName(category.getName()) != null) {
                    return HttpStatus.CONFLICT;
                }
            }
        }
        categoryFromDB.setName(category.getName());
        categoryFromDB.setDescription(category.getDescription());
        if(!updateOnlyName){
            List<Criterion> criteriaToSave =new ArrayList<>();
            category.getCriteria().forEach(criterion -> criteriaToSave.add(criterionService.findById(criterion.getId())));
            categoryFromDB.setCriteria(criteriaToSave);
        }
        save(categoryFromDB);
        return HttpStatus.OK;
    }

    public HttpStatus deleteCategoryFlow(long categoryId) {
        Category categoryFromDB = findById(categoryId);
        if (!categoryFromDB.getPerformances().isEmpty()) {
            return HttpStatus.CONFLICT;
        }
        delete(categoryFromDB);
        return HttpStatus.OK;
    }
}
