package ru.akhitev.megaplan.progress.daily.report;

import org.junit.Before;
import org.junit.Test;
import ru.akhitev.megaplan.progress.daily.entity.Employee;
import ru.akhitev.megaplan.progress.daily.entity.Progress;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XlsSheetCreatorSpec {
    XlsSheetCreator creator;
    List<Progress> progresses;

    @Before
    public void setUp() {
        creator = new XlsSheetCreator();

        progresses = new ArrayList<>();
        Employee employee = new Employee();
        employee.setName("Анна");
        Progress progress1 = new Progress();
        progress1.setEmployee(employee);
        progress1.setProgressDate(LocalDate.of(2019, 2, 2));
        progress1.setTookInWork(202);
        progress1.setOurRefuges(37);
        progress1.setAllCausesReserve(24);
        progress1.setLowReserve(20);
        progress1.setLaunchedInWork(100);
        progress1.setCandidateRefuges(10);
        progress1.setLaunchesAverageTerm(10.2);
        progresses.add(progress1);

        Progress progress2 = new Progress();
        progress2.setEmployee(employee);
        progress2.setProgressDate(LocalDate.of(2019, 2, 1));
        progress2.setTookInWork(204);
        progress2.setOurRefuges(35);
        progress2.setAllCausesReserve(21);
        progress2.setLowReserve(15);
        progress2.setLaunchedInWork(97);
        progress2.setCandidateRefuges(11);
        progress2.setLaunchesAverageTerm(12.9);
        progresses.add(progress2);

        Progress progress3 = new Progress();
        progress3.setEmployee(employee);
        progress3.setProgressDate(LocalDate.of(2019, 2, 4));
        progress3.setTookInWork(190);
        progress3.setOurRefuges(34);
        progress3.setAllCausesReserve(11);
        progress3.setLowReserve(11);
        progress3.setLaunchedInWork(93);
        progress3.setCandidateRefuges(19);
        progress3.setLaunchesAverageTerm(11.4);
        progresses.add(progress3);
    }

    @Test
    public void whenCorrectDataThenMakeReport() throws IOException {
        creator.prepareSheet(progresses, "testReport.xlsx");
    }
}
