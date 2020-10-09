package app.service;

import app.entity.Performance;
import app.repositories.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService implements CRUDInterface<Performance> {

    @Autowired
    PerformanceRepository performanceRepository;

    @Override
    public void save(Performance performance) {
        performanceRepository.save(performance);
    }

    @Override
    public Performance findById(long id) {
        return performanceRepository.findById(id).orElse(null);
    }

    public Performance findByName(String name) {
        return performanceRepository.findByName(name);
    }

    @Override
    public List<Performance> findAll() {
        return performanceRepository.findAll();
    }

    @Override
    public void delete(Performance performance) {
        performanceRepository.delete(performance);
    }

    @Override
    public void deleteAll() {
        performanceRepository.deleteAll();
    }

    public HttpStatus savePerformanceFlow(Performance performance) {
        performanceRepository.saveAndFlush(performance);
        return HttpStatus.CREATED;
    }

    public HttpStatus deletePerformanceFlow(long id) {
        return HttpStatus.OK;
    }
}

