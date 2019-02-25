package ru.akhitev.megaplan.progress.daily.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import ru.akhitev.megaplan.progress.daily.Launcher;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.repo.EmployeeRepository;
import ru.akhitev.megaplan.progress.daily.repo.ProgressRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class TableInputController extends AbstractController {
    private EmployeeRepository employeeRepository;
    private ProgressRepository progressRepository;
    private ObservableList<Progress> progresses = FXCollections.observableArrayList();
    public TableView<Progress> progressTable;
    public Button save;

    @FXML
    public void initialize() {
        employeeRepository = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        prepareTable();
        prepareColumns();
        prepareTableRows();
        prepareContextMenu();
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            progressTable.getItems().forEach(p -> {
                p.setProgressDate(LocalDate.now());
                p.validateForSave();
                progressRepository.save(p);
            });
        });
    }

    private void prepareTable() {
        progressTable.setPlaceholder(new Text("No content in table"));
        progressTable.setItems(progresses);
        progressTable.getSelectionModel().setCellSelectionEnabled(true);
        progressTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void prepareColumns() {
        TableColumn<Progress, String> employeeName = new TableColumn<>("Сотрудник");
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employee"));
        employeeName.setPrefWidth(40);
        progressTable.getColumns().add(employeeName);

        TableColumn<Progress, Integer> tookInWork = new TableColumn<>("Взято в работу");
        tookInWork.setPrefWidth(40);
        tookInWork.setCellValueFactory(new PropertyValueFactory<>("tookInWork"));
        tookInWork.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        progressTable.getColumns().add(tookInWork);

        TableColumn<Progress, Integer> ourRefuges = new TableColumn<>("Отказ (наш)");
        tookInWork.setPrefWidth(40);
        ourRefuges.setCellValueFactory(new PropertyValueFactory<>("ourRefuges"));
        progressTable.getColumns().add(ourRefuges);

        TableColumn<Progress, Integer> allCausesReserve = new TableColumn<>("Резерв (все причины)");
        tookInWork.setPrefWidth(40);
        allCausesReserve.setCellValueFactory(new PropertyValueFactory<>("allCausesReserve"));
        progressTable.getColumns().add(allCausesReserve);

        TableColumn<Progress, Integer> lowReserve = new TableColumn<>("Резерв (низкие)");
        tookInWork.setPrefWidth(40);
        lowReserve.setCellValueFactory(new PropertyValueFactory<>("lowReserve"));
        progressTable.getColumns().add(lowReserve);

        TableColumn<Progress, Integer> launchedInWork = new TableColumn<>("Выведенно в работу");
        tookInWork.setPrefWidth(40);
        launchedInWork.setCellValueFactory(new PropertyValueFactory<>("launchedInWork"));
        progressTable.getColumns().add(launchedInWork);

        TableColumn<Progress, Integer> candidateRefuges = new TableColumn<>("Отказ кандидата");
        tookInWork.setPrefWidth(40);
        candidateRefuges.setCellValueFactory(new PropertyValueFactory<>("candidateRefuges"));
        progressTable.getColumns().add(candidateRefuges);

        TableColumn<Progress, Double> launchesAverageTerm = new TableColumn<>("Средний срок вывода");
        tookInWork.setPrefWidth(40);
        launchesAverageTerm.setCellValueFactory(new PropertyValueFactory<>("launchesAverageTerm"));
        progressTable.getColumns().add(launchesAverageTerm);

        for (Progress progress : progresses) {
            progress.validateForSave();
            progressRepository.save(progress);
        }


    }

    private void prepareTableRows() {
        int employeeCount = employeeRepository.findAll().size();
        IntStream.range(0, employeeCount).forEach(i -> progressTable.getItems().add(new Progress()));
    }

    private void prepareContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem pasteMenuItem = new MenuItem("Вставить");
        pasteMenuItem.setOnAction(event -> pasteMenuHandler());
        contextMenu.getItems().add(pasteMenuItem);
        progressTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(progressTable, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void pasteMenuHandler() {
        String pasteString = Clipboard.getSystemClipboard().getString();
        System.out.println(pasteString);
        String[][] splitted = split(pasteString);
        String[] header = splitted[0];
        Progress progress = parsePastedProgress(splitted[1], header);
        List<Progress> progresses = new ArrayList<>();
        IntStream.range(1, splitted.length).forEach(i -> progresses.add(parsePastedProgress(splitted[i], header)));
        progressTable.getItems().clear();
        progressTable.getItems().addAll(progresses);
    }

    private String[][] split(String pasteString) {
        String[] lines = pasteString.split("\n");
        int length = lines.length;
        String[][] splitted = new String[length][];
        IntStream.range(0, length).forEach(i -> splitted[i] = lines[i].split("\t"));
        return splitted;
    }

    private Progress parsePastedProgress(String[] row, String[] header) {
        Employee employee = employeeRepository.findByName(row[0]).get(0);
        Progress progress = new Progress();
        progress.setEmployee(employee);
        IntStream.range(1, row.length).forEach( i -> {
            if ("Взято в работу".equals(header[i].trim())) {
                progress.setTookInWork(Integer.parseInt(row[i]));
            }
            if ("Отказ наш".equals(header[i].trim())) {
                progress.setOurRefuges(Integer.parseInt(row[i]));
            }
            if ("Резерв, все причины".equals(header[i].trim())) {
                progress.setAllCausesReserve(Integer.parseInt(row[i]));
            }
            if ("Резерв, низкие уровни".equals(header[i].trim())) {
                progress.setLowReserve(Integer.parseInt(row[i]));
            }
            if ("Выведено в работу".equals(header[i].trim())) {
                progress.setLaunchedInWork(Integer.parseInt(row[i]));
            }
            if ("Отказ кандидата".equals(header[i].trim())) {
                progress.setCandidateRefuges(Integer.parseInt(row[i]));
            }
            if ("Ср срок вывода, дни".equals(header[i].trim())) {
                progress.setLaunchesAverageTerm(Double.parseDouble(row[i].replace(",", ".")));
            }
        });
        return progress;
    }
}
