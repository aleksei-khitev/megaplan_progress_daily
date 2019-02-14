package ru.akhitev.megaplan.progress.daily.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;

import java.time.LocalDate;
import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    List<Progress> findByEmployee(Employee employee);

    @Query("select p from Progress p where p.progressDate >= :startOfPeriod and p.employee = :employee")
    List<Progress> findByEmployeeCurrentPeriod(@Param("employee") Employee employee, @Param("startOfPeriod") LocalDate startOfPeriod);
}
