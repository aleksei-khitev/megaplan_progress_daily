package ru.akhitev.megaplan.progress.daily.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.akhitev.megaplan.progress.daily.Launcher;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.repo.EmployeeRepository;

import java.util.stream.Collectors;

public class EmployeeController extends AbstractController {
    public ListView<String> employees;
    private EmployeeRepository employeeRepo;
    public TextField employeeName;
    public Button addButton;

    @FXML
    public void initialize() {
        employeeRepo = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> add());
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

}
