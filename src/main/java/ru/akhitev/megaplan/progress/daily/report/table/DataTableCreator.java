package ru.akhitev.megaplan.progress.daily.report.table;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.report.rows.DataTableRows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public class DataTableCreator extends AbstractTableCreator {
    private static final String HEADER_TEMPLATE = "%s: просто показатели";
    public DataTableCreator(String employeeName, List<Progress> progresses, XSSFWorkbook workbook, XSSFSheet sheet) {
        super(employeeName, progresses, workbook, sheet);
    }

    public void create() {
        prepareHeaderCollumn();
        IntStream.range(0, progresses.size()).forEach(i -> {
            int columnNumber = i + 1;
            fillColumn(progresses.get(i), columnNumber);
        });
        IntStream.range(0, progresses.size() + 1).forEach((columnIndex) -> sheet.autoSizeColumn(columnIndex));
    }

    private void prepareHeaderCollumn() {
        writeEmployeeName(String.format(HEADER_TEMPLATE, employeeName));
        sheet.createRow(1);
        CellStyle rowHeaderCellStyle = workbook.createCellStyle();
        rowHeaderCellStyle.setFont(rowHeaderFont);
        for (DataTableRows dataTableRows : DataTableRows.values()) {
            prepareHeaderCell(sheet, dataTableRows, rowHeaderCellStyle);
        }
    }

    private void writeEmployeeName(String name) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Cell employeeNameCell = sheet.createRow(0).createCell(0);
        employeeNameCell.setCellValue(name);
        employeeNameCell.setCellStyle(headerCellStyle);
    }

    private void fillColumn(Progress progress, int columnNumber) {
        writeDateHeader(progress.getProgressDate(), columnNumber);
        writeData(progress, columnNumber);
    }

    private void writeDateHeader(LocalDate progressDate, int columnNumber) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(dateHeaderFont);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        drawBorders(cellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.getRow(1).createCell(columnNumber);
        headerCell.setCellValue(progressDate.format(DateTimeFormatter.ofPattern(DATE_TEMPLATE)));
        headerCell.setCellStyle(cellStyle);
    }

    private void writeData(Progress progress, int columnNumber) {
        writeIntegerData(sheet, workbook, dataFont, DataTableRows.TOOK_IN_WORK, columnNumber, progress.getTookInWork());
        writeIntegerData(sheet, workbook, dataFont, DataTableRows.OUR_REFUGES, columnNumber, progress.getOurRefuges());
        writeIntegerData(sheet, workbook, dataFont, DataTableRows.ALL_CAUSES_RESERVE, columnNumber, progress.getAllCausesReserve());
        writeIntegerData(sheet, workbook, dataFont, DataTableRows.LAUNCHED_IN_WORK, columnNumber, progress.getLaunchedInWork());
        writeIntegerData(sheet, workbook, dataFont, DataTableRows.CANDIDATE_REFUGES, columnNumber, progress.getCandidateRefuges());
        writeDoubleData(sheet, workbook, dataFont, DataTableRows.LAUNCHES_AVERAGE_TERM, columnNumber, progress.getLaunchesAverageTerm());
        writeDoubleData(sheet, workbook, dataFont, DataTableRows.QUALITY_INDEX, columnNumber, progress.calculateQualityIndex());
        writePercentageData(sheet, workbook, dataFont, DataTableRows.LAUNCH_PORTION, columnNumber, progress.calculateLaunchPortion());
        writePercentageData(sheet, workbook, dataFont, DataTableRows.REFUGES_PORTION, columnNumber, progress.calculateRefugesPortion());
    }
}
