package ru.akhitev.megaplan.progress.daily.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.akhitev.megaplan.progress.daily.Launcher;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.repo.EmployeeRepository;
import ru.akhitev.megaplan.progress.daily.repo.ProgressRepository;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeController extends AbstractController {
    public ListView<String> employees;
    public TextField employeeName;
    public Button addButton;
    public Button removeButton;
    private EmployeeRepository employeeRepo;
    private ProgressRepository progressRepository;

    @FXML
    public void initialize() {
        employeeRepo = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> add());
        removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> remove());
        updateEmployeeList();
    }

    private void updateEmployeeList() {
        employees.getItems().clear();
        employees.setItems(FXCollections.observableArrayList(employeeRepo.findAll()
                .stream().map(Employee::getName).collect(Collectors.toList())));
    }

    private void add() {
        Employee employee = new Employee();
        try {
            employee.setName(employeeName.getText());
            employee.validateForSave();
            employeeRepo.save(employee);
            employeeName.setText("");
            updateEmployeeList();
            showMessage("Сотрудник добавлен");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    private void remove() {
        String name = employees.getSelectionModel().getSelectedItem();
        Employee employee = employeeRepo.findByName(name).get(0);
        List<Progress> employeeProgresses = progressRepository.findByEmployee(employee);
        if (employeeProgresses != null) {
            employeeProgresses.forEach(progressRepository::delete);
        }
        employeeRepo.delete(employee);
        updateEmployeeList();
    }

}
