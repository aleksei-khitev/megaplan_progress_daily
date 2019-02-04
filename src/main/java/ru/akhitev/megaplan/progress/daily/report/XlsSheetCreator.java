package ru.akhitev.megaplan.progress.daily.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import ru.akhitev.megaplan.progress.daily.entity.Progress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class XlsSheetCreator {
    private String employeeName;
    private List<Progress> progresses;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public XlsSheetCreator(List<Progress> progresses, XSSFWorkbook workbook, String employeeName) {
        this.employeeName = employeeName;
        this.progresses = progresses;
        this.workbook = workbook;
        sheet = workbook.createSheet(employeeName);
    }

    public void prepareSheet() {
        progresses.sort((o1, o2) -> {
            if (o1.getProgressDate().isAfter(o2.getProgressDate())) {
                return 1;
            } else if (o2.getProgressDate().isAfter(o1.getProgressDate())) {
                return -1;
            } else {
                return 0;
            }
        });
        prepareHeaderCollumn();
        IntStream.range(0, progresses.size()).forEach(i -> {
            int columnNumber = i + 1;
            fillColumn(progresses.get(i), columnNumber);
        });
        IntStream.range(0, progresses.size() + 1).forEach((columnIndex) -> sheet.autoSizeColumn(columnIndex));
        XlsChartCreator chartCreator = new XlsChartCreator(sheet, progresses.size());
        chartCreator.drawGraphic(XlsReportRows.LAUNCHES_AVERAGE_TERM,13);
        chartCreator.drawGraphic(XlsReportRows.LAUNCH_PORTION, 23);
        chartCreator.drawGraphic(XlsReportRows.LAUNCHED_IN_WORK, 33);
        chartCreator.drawGraphic(XlsReportRows.REFUGES_PORTION, 43);
        chartCreator.drawGraphic(XlsReportRows.CANDIDATE_REFUGES, 53);
        chartCreator.drawGraphic(XlsReportRows.TOOK_IN_WORK, 63);
        chartCreator.drawGraphic(XlsReportRows.OUR_REFUGES, 73);
        chartCreator.drawGraphic(XlsReportRows.ALL_CAUSES_RESERVE, 83);
        chartCreator.drawGraphic(XlsReportRows.LOW_RESERVE, 93);
    }


    private void prepareHeaderCollumn() {
        writeEmployeeName(employeeName + ": динамика показателей");
        sheet.createRow(1);
        for (XlsReportRows xlsReportRows : XlsReportRows.values()) {
            Font rowHeaderFont = prepareFont();
            rowHeaderFont.setBold(false);
            rowHeaderFont.setFontHeightInPoints((short) 12);
            CellStyle rowHeaderCellStyle = prepareHeaderStyle(rowHeaderFont);
            xlsReportRows.prepareHeaderCell(sheet, rowHeaderCellStyle);
        }
    }

    private void writeEmployeeName(String name) {
        Font headerFont = prepareFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = prepareHeaderStyle(headerFont);
        headerCellStyle.setFont(headerFont);
        Cell employeeNameCell = sheet.createRow(0).createCell(0);
        employeeNameCell.setCellValue(name);
        employeeNameCell.setCellStyle(headerCellStyle);
    }

    private Font prepareFont() {
        return workbook.createFont();
    }

    private CellStyle prepareHeaderStyle(Font headerFont) {
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        return workbook.createCellStyle();
    }

    private void fillColumn(Progress progress, int columnNumber) {
        writeDateHeader(progress.getProgressDate(), columnNumber);
        writeData(progress, columnNumber);
    }

    private void writeDateHeader(LocalDate progressDate, int columnNumber) {
        Font font = prepareFont();
        font.setBold(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        font.setFontHeightInPoints((short) 12);
        CellStyle cellStyle = prepareHeaderStyle(font);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Cell headerCell = sheet.getRow(1).createCell(columnNumber);
        headerCell.setCellValue(progressDate.toString());
        headerCell.setCellStyle(cellStyle);
    }

    private void writeData(Progress progress, int columnNumber) {
        Font font = prepareFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        CellStyle cellStyle = prepareHeaderStyle(font);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XlsReportRows.TOOK_IN_WORK.writeIntegerData(sheet, workbook, columnNumber, progress.getTookInWork());
        XlsReportRows.OUR_REFUGES.writeIntegerData(sheet, workbook, columnNumber, progress.getOurRefuges());
        XlsReportRows.ALL_CAUSES_RESERVE.writeIntegerData(sheet, workbook, columnNumber, progress.getAllCausesReserve());
        XlsReportRows.LOW_RESERVE.writeIntegerData(sheet, workbook, columnNumber, progress.getLowReserve());
        XlsReportRows.LAUNCHED_IN_WORK.writeIntegerData(sheet, workbook, columnNumber, progress.getLaunchedInWork());
        XlsReportRows.CANDIDATE_REFUGES.writeIntegerData(sheet, workbook, columnNumber, progress.getCandidateRefuges());
        XlsReportRows.LAUNCHES_AVERAGE_TERM.writeDoubleData(sheet, workbook, columnNumber, progress.getLaunchesAverageTerm());
        XlsReportRows.QUALITY_INDEX.writeDoubleData(sheet, workbook, columnNumber, progress.calculateQualityIndex());
        XlsReportRows.LAUNCH_PORTION.writePercentageData(sheet, workbook, columnNumber, progress.calculateLaunchPortion());
        XlsReportRows.REFUGES_PORTION.writePercentageData(sheet, workbook, columnNumber, progress.calculateRefugesPortion());
    }

}
