package app.service;

import app.entity.Criterion;
import app.repositories.CriterionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriterionService implements CRUDInterface<Criterion> {

    @Autowired
    CriterionRepository repository;

    @Override
    public void save(Criterion criterion) {
        repository.save(criterion);
    }

    @Override
    public Criterion findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Criterion> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Criterion criterion) {
        repository.delete(criterion);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public Criterion findByName(String name) {
        return repository.findByName(name);
    }

    public HttpStatus saveCriterionFlow(Criterion criterion) {
        Criterion criterionFromDB;
        if (criterion.getId() == -1) {
            //check for unique name
            if (findByName(criterion.getName()) != null) {
                return HttpStatus.CONFLICT;
            }
            criterionFromDB = new Criterion();
        } else {
            criterionFromDB = findById(criterion.getId());
            if (!criterion.getName().equals(criterionFromDB.getName())) {
                //check for unique name
                if (findByName(criterion.getName()) != null) {
                    return HttpStatus.CONFLICT;
                }
            }
        }
        criterionFromDB.setName(criterion.getName());
        criterionFromDB.setDescription(criterion.getDescription());
        save(criterionFromDB);
        return HttpStatus.OK;
    }

    public HttpStatus deleteCriterionFlow(long criterionId) {
        Criterion criterionFromDB = findById(criterionId);
        if (!criterionFromDB.getMarks().isEmpty()) {
            return HttpStatus.CONFLICT;
        }
        delete(criterionFromDB);
        return HttpStatus.OK;
    }
}
