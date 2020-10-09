package app.repositories;

import app.entity.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
    Criterion findByName(String name);
}
