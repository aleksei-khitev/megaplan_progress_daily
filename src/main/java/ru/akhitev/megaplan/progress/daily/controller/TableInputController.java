package ru.akhitev.megaplan.progress.daily.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    public DatePicker editDate;
    public TableView<Progress> progressTable;
    public Button save;

    @FXML
    public void initialize() {
        employeeRepository = Launcher.getSpringContext().getBean(EmployeeRepository.class);
        progressRepository = Launcher.getSpringContext().getBean(ProgressRepository.class);
        editDate.setValue(LocalDate.now());
        prepareTable();
        prepareColumns();
        prepareTableRows();
        prepareContextMenu();
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            progressTable.getItems().forEach(p -> {
                p.setProgressDate(editDate.getValue());
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
        TableColumn<Progress, String> employeeName = new TableColumn<>();
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employee"));

        progressTable.getColumns().add(prepareColumnHeader(employeeName, "Сотрудник", 110));

        TableColumn<Progress, Integer> tookInWork = new TableColumn<>();
        tookInWork.setCellValueFactory(new PropertyValueFactory<>("tookInWork"));
        tookInWork.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        progressTable.getColumns().add(prepareColumnHeader(tookInWork, "Взято в работу", 40));

        TableColumn<Progress, Integer> ourRefuges = new TableColumn<>();
        ourRefuges.setCellValueFactory(new PropertyValueFactory<>("ourRefuges"));
        progressTable.getColumns().add(prepareColumnHeader(ourRefuges, "Отказ (наш)", 40));

        TableColumn<Progress, Integer> allCausesReserve = new TableColumn<>();
        allCausesReserve.setCellValueFactory(new PropertyValueFactory<>("allCausesReserve"));
        progressTable.getColumns().add(prepareColumnHeader(allCausesReserve, "Резерв (все причины)", 40));

        TableColumn<Progress, Integer> lowReserve = new TableColumn<>();
        lowReserve.setCellValueFactory(new PropertyValueFactory<>("lowReserve"));
        progressTable.getColumns().add(prepareColumnHeader(lowReserve, "Резерв (низкие)", 40));

        TableColumn<Progress, Integer> launchedInWork = new TableColumn<>();
        launchedInWork.setCellValueFactory(new PropertyValueFactory<>("launchedInWork"));
        progressTable.getColumns().add(prepareColumnHeader(launchedInWork, "Выведенно в работу", 40));

        TableColumn<Progress, Integer> candidateRefuges = new TableColumn<>();
        candidateRefuges.setCellValueFactory(new PropertyValueFactory<>("candidateRefuges"));
        progressTable.getColumns().add(prepareColumnHeader(candidateRefuges, "Отказ кандидата", 40));

        TableColumn<Progress, Double> launchesAverageTerm = new TableColumn<>();
        launchesAverageTerm.setCellValueFactory(new PropertyValueFactory<>("launchesAverageTerm"));
        progressTable.getColumns().add(prepareColumnHeader(launchesAverageTerm, "Средний срок вывода", 60));

        for (Progress progress : progresses) {
            progress.validateForSave();
            progressRepository.save(progress);
        }
    }

    private <T extends Object> TableColumn<Progress, T>  prepareColumnHeader(TableColumn<Progress, T> column, String title, int prefWidth) {
        column.setPrefWidth(prefWidth);
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
        VBox vbox = new VBox(label);
        vbox.setRotate(-90);
        vbox.setPadding(new Insets(5, 5, 5, 5));
        Group group = new Group(vbox);
        column.setGraphic(group);
        return column;
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
