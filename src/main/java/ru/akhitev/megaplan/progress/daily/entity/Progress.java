package ru.akhitev.megaplan.progress.daily.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "progress")
@SequenceGenerator(name = "seq", initialValue = 20)
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Integer id;

    @ManyToOne @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "progress_date", nullable = false)
    private LocalDate progressDate;

    @Column(name = "took_in_work")
    private Integer tookInWork;

    @Column(name = "our_refuges")
    private Integer ourRefuges;

    @Column(name = "all_causes_reserve")
    private Integer allCausesReserve;

    @Column(name = "low_reserve")
    private Integer lowReserve;

    @Column(name = "launched_in_work")
    private Integer launchedInWork;

    @Column(name = "candidate_refuges")
    private Integer candidateRefuges;

    @Column(name = "launches_average_term")
    private Double launchesAverageTerm;

    public String getFormattedLaunchPortion() {
        return String.format("%.2f", calculateLaunchPortion()*100) + "%";
    }

    public double calculateLaunchPortion() {
        if (validateDataForLaunchPortion()) {
            double fraction = (double) (tookInWork - ourRefuges - allCausesReserve);
            return (launchedInWork / fraction);
        } else {
            return 0;
        }
    }

    private boolean validateDataForLaunchPortion() {
        return launchedInWork != null
                && tookInWork != null
                && ourRefuges != null
                && allCausesReserve != null
                && ((tookInWork - ourRefuges - allCausesReserve) != 0);
    }

    public String getFormattedRefugesPortion() {
        return String.format("%.2f", calculateRefugesPortion()*100) + "%";
    }

    public double calculateRefugesPortion() {
        if (validateDataForRefugesPorion()) {
            return (double) candidateRefuges / tookInWork;
        } else {
            return 0;
        }
    }

    private boolean validateDataForRefugesPorion() {
        return candidateRefuges != null
                && tookInWork != null
                && tookInWork > 0;
    }

    public String getFormattedQualityIndex() {
        return String.format("%.2f", calculateQualityIndex());
    }

    public double calculateQualityIndex() {
        if (validateForQualityIndex()) {
            return calculateLaunchPortion() / calculateRefugesPortion();
        } else {
            return 0;
        }
    }

    private boolean validateForQualityIndex() {
        return validateDataForLaunchPortion()
                && validateDataForRefugesPorion()
                && calculateRefugesPortion() > 0;
    }

    public void validateForSave() {
        if (employee == null) {
            throw new IllegalArgumentException("Работник не выбран", null);
        }
        if (progressDate == null) {
            throw new IllegalArgumentException("Дата не выбрана", null);
        }
        if (tookInWork == null) {
            throw new IllegalArgumentException("Взято в работу не задано", null);
        }
        if (ourRefuges == null) {
            throw new IllegalArgumentException("Отказ (наш) не задано", null);
        }
        if (allCausesReserve == null) {
            throw new IllegalArgumentException("Резерв (все причины) не задано", null);
        }
        if (launchedInWork == null) {
            throw new IllegalArgumentException("Выведенно в работу не задано", null);
        }
        if (candidateRefuges == null) {
            throw new IllegalArgumentException("Отказ кандидата не задано", null);
        }
        if (launchesAverageTerm == null) {
            throw new IllegalArgumentException("Средний срок вывода не задано", null);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(LocalDate progressDate) {
        this.progressDate = progressDate;
    }

    public Integer getTookInWork() {
        return tookInWork;
    }

    public void setTookInWork(Integer tookInWork) {
        this.tookInWork = tookInWork;
    }

    public Integer getOurRefuges() {
        return ourRefuges;
    }

    public void setOurRefuges(Integer ourRefuges) {
        this.ourRefuges = ourRefuges;
    }

    public Integer getAllCausesReserve() {
        return allCausesReserve;
    }

    public void setAllCausesReserve(Integer allCausesReserve) {
        this.allCausesReserve = allCausesReserve;
    }

    public Integer getLowReserve() {
        return lowReserve;
    }

    public void setLowReserve(Integer lowReserve) {
        this.lowReserve = lowReserve;
    }

    public Integer getLaunchedInWork() {
        return launchedInWork;
    }

    public void setLaunchedInWork(Integer launchedInWork) {
        this.launchedInWork = launchedInWork;
    }

    public Integer getCandidateRefuges() {
        return candidateRefuges;
    }

    public void setCandidateRefuges(Integer candidateRefuges) {
        this.candidateRefuges = candidateRefuges;
    }

    public Double getLaunchesAverageTerm() {
        return launchesAverageTerm;
    }

    public void setLaunchesAverageTerm(Double launchesAverageTerm) {
        this.launchesAverageTerm = launchesAverageTerm;
    }
}
