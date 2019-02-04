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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController extends AbstractController {
    private EmployeeRepository employeeRepository;
    private ProgressRepository progressRepository;
    public Button makeReport;

    @FXML
    public void initialize() {
        employeeRepository = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        defineHandlers();
    }

    private void defineHandlers() {
        makeReport.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> prepareReport());
    }

    private void prepareReport() {
        XlsWorkReportCreator reportCreator = new XlsWorkReportCreator();
        try {
            reportCreator.makeReport(prepareData(), prepareReportName());
            showMessage("Отчет сформирован");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    private Map<String, List<Progress>> prepareData() {
        Map<String, List<Progress>> allProgress = new HashMap<>();
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach( e -> {
            List<Progress> progresses = progressRepository.findByEmployee(e);
            if (!progresses.isEmpty()) {
                allProgress.put(e.getName(), progresses);
            }
        });
        return allProgress;
    }

    private String prepareReportName() {
        return LocalDate.now().toString() + ".xlsx";
    }
}
