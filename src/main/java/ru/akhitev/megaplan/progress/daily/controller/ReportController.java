package ru.akhitev.megaplan.progress.daily.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ru.akhitev.megaplan.progress.daily.Launcher;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.repo.EmployeeRepository;
import ru.akhitev.megaplan.progress.daily.repo.ProgressRepository;
import ru.akhitev.megaplan.progress.daily.report.XlsWorkReportCreator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportController extends AbstractController {
    private EmployeeRepository employeeRepository;
    private ProgressRepository progressRepository;
    public Button makeReportForWholePeriod;
    public Button makeReportForCurrentPeriodWithStep7;
    public Button makeReportForCurrentPeriod;

    @FXML
    public void initialize() {
        employeeRepository = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        defineHandlers();
    }

    private void defineHandlers() {
        makeReportForCurrentPeriod
                .addEventHandler(MouseEvent.MOUSE_CLICKED,
                        event -> prepareReport(
                                    prepareData(currentPeriodFunction)));
        makeReportForCurrentPeriodWithStep7.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> prepareReport(
                        prepareData(e -> currentPeriodFunction.apply(e)
                        .stream().filter( p -> p.getProgressDate().getDayOfWeek().equals(DayOfWeek.FRIDAY)).collect(Collectors.toList()))));
        makeReportForWholePeriod
                .addEventHandler(MouseEvent.MOUSE_CLICKED,
                        event -> prepareReport(
                                prepareData(e -> progressRepository.findByEmployee(e))));
    }

    private Function<Employee, List<Progress>> currentPeriodFunction = e -> {
        final int periodBorder = 8;
        final LocalDate startOfPeriod;
        final LocalDate endOfPeriod;
        final LocalDate currentDate = LocalDate.now();
        if (currentDate.getDayOfMonth() < periodBorder) {
            startOfPeriod = LocalDate.of(currentDate.getYear(), currentDate.getMonth().minus(1), periodBorder);
            endOfPeriod = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), periodBorder);
        } else {
            startOfPeriod = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), periodBorder);
            endOfPeriod = LocalDate.of(currentDate.getYear(), currentDate.getMonth().plus(1), periodBorder);
        }
        return progressRepository.findByEmployeeCurrentPeriod(e, startOfPeriod, endOfPeriod);
    };

    private void prepareReport(Map<String, List<Progress>> data) {
        XlsWorkReportCreator reportCreator = new XlsWorkReportCreator();
        try {
            reportCreator.makeReport(data, prepareReportName());
            showMessage("Отчет сформирован");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    private Map<String, List<Progress>> prepareData(Function<Employee, List<Progress>> function) {
        Map<String, List<Progress>> allProgress = new HashMap<>();
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach( e -> {
            List<Progress> progresses = function.apply(e);
            if (!progresses.isEmpty() && progresses.size() > 1) {
                allProgress.put(e.getName(), progresses);
            }
        });
        return allProgress;
    }

    private String prepareReportName() {
        String fileExtension = ".xlsx";
        return LocalDate.now().toString() + fileExtension;
    }
}
