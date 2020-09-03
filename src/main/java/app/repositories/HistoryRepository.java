package app.repositories;

import app.entity.LogsRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<LogsRep, Long> {
}

