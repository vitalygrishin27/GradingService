package app.repositories;

import app.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
Configuration findByConfigKey(String configKey);
}
