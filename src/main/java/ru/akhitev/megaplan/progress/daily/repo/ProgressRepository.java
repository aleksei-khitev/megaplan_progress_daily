package ru.akhitev.megaplan.progress.daily.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    List<Progress> findByEmployee(Employee employee);
}
