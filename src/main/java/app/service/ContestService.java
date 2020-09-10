package app.service;

import app.entity.Configuration;
import app.entity.Contest;
import app.repositories.CRUDInterface;
import app.repositories.ConfigurationRepository;
import app.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestService implements CRUDInterface<Contest> {

    @Autowired
    ContestRepository repository;

    @Override
    public void save(Contest contest) {
        repository.save(contest);
    }

    @Override
    public Contest findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Contest> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Contest contets) {
        repository.delete(contets);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public Contest findByName(String name) {
        return repository.findByName(name);
    }

    public HttpStatus saveFlow(Contest contest) {
        return HttpStatus.OK;
    }

    public HttpStatus deleteFlow(long contestId) {
        return HttpStatus.OK;
    }
}
