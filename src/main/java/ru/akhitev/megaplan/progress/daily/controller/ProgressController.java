package ru.akhitev.megaplan.progress.daily.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.akhitev.megaplan.progress.daily.Launcher;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.repo.EmployeeRepository;
import ru.akhitev.megaplan.progress.daily.repo.ProgressRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgressController extends AbstractController {
    private EmployeeRepository employeeRepository;
    private ProgressRepository progressRepository;
    private Progress progress;
    public TextField tookInWork;
    public TextField ourRefuges;
    public TextField allCausesReserve;
    public TextField lowReserve;
    public TextField launchedInWork;
    public TextField launchPortion;
    public TextField refugesPortion;
    public TextField candidateRefuges;
    public TextField launchesAverageTerm;
    public TextField qualityIndex;
    public DatePicker editDate;
    public ComboBox<String> editEmployee;
    public Button open;
    public Button saveButton;
    public Label status;

    @FXML
    public void initialize() {
        employeeRepository = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        initData();
        defineHandlers();
    }

    private void initData() {
        updateEditEmployeeList();
        clearProgressAndFields();
        editDate.setValue(LocalDate.now());
    }

    private void clearProgressAndFields() {
        tookInWork.clear();
        ourRefuges.clear();
        allCausesReserve.clear();
        lowReserve.clear();
        launchedInWork.clear();
        launchPortion.clear();
        refugesPortion.clear();
        candidateRefuges.clear();
        launchesAverageTerm.clear();
        qualityIndex.clear();
        progress = new Progress();
        tookInWork.requestFocus();
    }

    private void defineHandlers() {
        open.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> open());
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> save());
        editEmployee.addEventHandler(ComboBox.ON_SHOWING, e -> updateEditEmployeeList());
        tookInWork.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setTookInWork(Integer.valueOf(newValue));
                setLaunchPortionIfEnouphData();
                setRefugesPortionIfEnouphData();
            }
        });
        ourRefuges.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setOurRefuges(Integer.valueOf(newValue));
                setLaunchPortionIfEnouphData();
            }
        });
        lowReserve.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setLowReserve(Integer.valueOf(newValue));
            }
        });
        allCausesReserve.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setAllCausesReserve(Integer.valueOf(newValue));
                setLaunchPortionIfEnouphData();
            }
        });
        launchedInWork.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setLaunchedInWork(Integer.valueOf(newValue));
                setLaunchPortionIfEnouphData();
            }
        });
        candidateRefuges.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setCandidateRefuges(Integer.valueOf(newValue));
                setRefugesPortionIfEnouphData();
            }
        });
        launchesAverageTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                progress.setLaunchesAverageTerm(Double.valueOf(newValue.replaceAll(",", ".")));
            }
        });
    }

    private void updateEditEmployeeList() {
        editEmployee.getItems().clear();
        editEmployee.getItems().addAll(employeeRepository.findAll().stream().map(Employee::getName).collect(Collectors.toList()));
    }

    private void setLaunchPortionIfEnouphData() {
        launchPortion.setText(progress.getFormattedLaunchPortion());
        updateIndexIfEnouphData();
    }

    private void setRefugesPortionIfEnouphData() {
        refugesPortion.setText(progress.getFormattedRefugesPortion());
        updateIndexIfEnouphData();
    }

    private void updateIndexIfEnouphData() {
        qualityIndex.setText(progress.getFormattedQualityIndex());
    }

    private void open() {
        getEmployeeFromDao().ifPresent(employee -> {
            LocalDate date = editDate.getValue();
            List<Progress> progresses = progressRepository.findByEmployeeAndProgressDate(employee, date);
            if (!progresses.isEmpty()) {
                progress = progresses.get(0);
                tookInWork.setText(String.valueOf(progress.getTookInWork()));
                ourRefuges.setText(String.valueOf(progress.getOurRefuges()));
                allCausesReserve.setText(String.valueOf(progress.getAllCausesReserve()));
                lowReserve.setText(String.valueOf(progress.getLowReserve()));
                launchedInWork.setText(String.valueOf(progress.getLaunchedInWork()));
                candidateRefuges.setText(String.valueOf(progress.getCandidateRefuges()));
                launchesAverageTerm.setText(String.valueOf(progress.getLaunchesAverageTerm()));
            }
        });
    }

    private void save() {
        try {
            validate();
            progressRepository.save(progress);
            status.setText("Данные сохранены для " + progress.getEmployee().getName());
            clearProgressAndFields();
        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }

    private void validate() {
        String selectedEmployeeName = editEmployee.getValue();
        if (selectedEmployeeName == null || selectedEmployeeName.length() == 0) {
            throw new IllegalArgumentException("Работник не выбран.", null);
        }
        progress.setEmployee(getEmployeeFromDao().get());
        if (editDate.getValue() == null) {
            throw new IllegalArgumentException("Дата не выбрана", null);
        }
        progress.setProgressDate(editDate.getValue());
        progress.validateForSave();
    }

    private Optional<Employee> getEmployeeFromDao() {
        List<Employee> employees = employeeRepository.findByName(editEmployee.getValue());
        if (employees.size() > 0) {
            return Optional.of(employees.get(0));
        }
        return Optional.empty();
    }
}
