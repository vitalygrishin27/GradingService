package app.service;

import app.entity.Contest;
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
    public void delete(Contest contest) {
        repository.delete(contest);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public Contest findByName(String name) {
        return repository.findByName(name);
    }

    public HttpStatus saveFlow(Contest contest) {
        Contest contestFromDB;
        if (contest.getId() == -1) {
            //check for unique name
            if (findByName(contest.getName()) != null) {
                return HttpStatus.CONFLICT;
            }
            contestFromDB = new Contest();
        } else {
            contestFromDB = findById(contest.getId());
            if (!contest.getName().equals(contestFromDB.getName())) {
                //check for unique name
                if (findByName(contest.getName()) != null) {
                    return HttpStatus.CONFLICT;
                }
            }
        }
        contestFromDB.setName(contest.getName());
        contestFromDB.setPhoto(contest.getPhoto());
        contestFromDB.setCategories(contest.getCategories());
        save(contestFromDB);
        return HttpStatus.OK;
    }

    public HttpStatus deleteFlow(long contestId) {
        Contest contestFromDB = findById(contestId);
        if (!contestFromDB.getUsers().isEmpty()) {
            return HttpStatus.CONFLICT;
        }
        delete(contestFromDB);
        return HttpStatus.OK;
    }
}
