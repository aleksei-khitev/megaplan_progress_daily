package ru.akhitev.megaplan.progress.daily.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akhitev.megaplan.progress.daily.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByName(String name);
}
