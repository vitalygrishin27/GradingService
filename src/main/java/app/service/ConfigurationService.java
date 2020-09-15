package app.service;

import app.entity.Configuration;
import app.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService implements CRUDInterface<Configuration> {

    @Autowired
    ConfigurationRepository repository;

    @Override
    public void save(Configuration configuration) {
        repository.save(configuration);
    }

    @Override
    public Configuration findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Configuration> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Configuration configuration) {
        repository.delete(configuration);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public Configuration findByConfigKey(String key) {
        return repository.findByConfigKey(key);
    }

    public HttpStatus saveFlow(List<Configuration> configurations) {
        for (Configuration configuration : configurations
        ) {
            Configuration configInDB = findByConfigKey(configuration.getConfigKey());
            if (configInDB == null) {
                save(configuration);
            } else {
                configInDB.setConfigValue(configuration.getConfigValue());
                save(configInDB);
            }
        }
        return HttpStatus.OK;
    }

    public HttpStatus deleteFlow(String configKey) {
        delete(findByConfigKey(configKey));
        return HttpStatus.OK;
    }
}
