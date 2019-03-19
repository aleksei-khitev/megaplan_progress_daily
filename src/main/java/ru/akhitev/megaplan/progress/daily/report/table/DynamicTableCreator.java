package ru.akhitev.megaplan.progress.daily.report.table;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.report.rows.DataTableRows;
import ru.akhitev.megaplan.progress.daily.report.rows.DynamicTableRows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public class DynamicTableCreator extends AbstractTableCreator {
    private static final String HEADER_TEMPLATE = "%s: динамика показателей";
    private static final String DYNAMIC_FORMULA = "(100-%s*100/%s)*(-1)/100";
    private static final int HEADER_ROW_NUMBER = 12;

    public DynamicTableCreator(String employeeName, List<Progress> progresses, XSSFWorkbook workbook, XSSFSheet sheet) {
        super(employeeName, progresses, workbook, sheet);
    }

    public void create() {
        prepareHeaderCollumn();
        IntStream.range(1, progresses.size()).forEach(i -> {
            fillColumn(progresses.get(i), i);
        });
        IntStream.range(0, progresses.size() + 1).forEach((columnIndex) -> sheet.autoSizeColumn(columnIndex));
    }

    private void prepareHeaderCollumn() {
        writeEmployeeName(String.format(HEADER_TEMPLATE, employeeName));
        CellStyle rowHeaderCellStyle = workbook.createCellStyle();
        rowHeaderCellStyle.setFont(rowHeaderFont);
        for (DynamicTableRows dataTableRows : DynamicTableRows.values()) {
            prepareHeaderCell(sheet, dataTableRows, rowHeaderCellStyle);
        }
    }

    private void writeEmployeeName(String name) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Cell employeeNameCell = sheet.createRow(HEADER_ROW_NUMBER).createCell(0);
        employeeNameCell.setCellValue(name);
        employeeNameCell.setCellStyle(headerCellStyle);
    }

    private void fillColumn(Progress progress, int columnNumber) {
        writeDateHeader(progress.getProgressDate(), columnNumber);
        writeData(columnNumber);
    }

    private void writeDateHeader(LocalDate progressDate, int columnNumber) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(dateHeaderFont);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        drawBorders(cellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.getRow(HEADER_ROW_NUMBER).createCell(columnNumber);
        headerCell.setCellValue(progressDate.format(DateTimeFormatter.ofPattern(DATE_TEMPLATE)));
        headerCell.setCellStyle(cellStyle);
    }

    private void writeData(int columnNumber) {
        writeFormula(DataTableRows.TOOK_IN_WORK, DynamicTableRows.TOOK_IN_WORK, columnNumber);
        writeFormula(DataTableRows.OUR_REFUGES, DynamicTableRows.OUR_REFUGES, columnNumber);
        writeFormula(DataTableRows.ALL_CAUSES_RESERVE, DynamicTableRows.ALL_CAUSES_RESERVE, columnNumber);
        writeFormula(DataTableRows.LAUNCHED_IN_WORK, DynamicTableRows.LAUNCHED_IN_WORK, columnNumber);
        writeFormula(DataTableRows.CANDIDATE_REFUGES, DynamicTableRows.CANDIDATE_REFUGES, columnNumber);
        writeFormula(DataTableRows.LAUNCHES_AVERAGE_TERM, DynamicTableRows.LAUNCHES_AVERAGE_TERM, columnNumber);
        writeFormula(DataTableRows.QUALITY_INDEX, DynamicTableRows.QUALITY_INDEX, columnNumber);
        writeFormula(DataTableRows.LAUNCH_PORTION, DynamicTableRows.LAUNCH_PORTION, columnNumber);
        writeFormula(DataTableRows.REFUGES_PORTION, DynamicTableRows.REFUGES_PORTION, columnNumber);
    }

    private void writeFormula(DataTableRows dataTableRow, DynamicTableRows dynamicTableRow, int columnNumber) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        drawBorders(cellStyle, BorderStyle.THIN);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
        Cell headerCell = sheet.getRow(dynamicTableRow.getRowNumber()).createCell(columnNumber);
        headerCell.setCellType(CellType.FORMULA);
        String valueNow = getCellAdress(dataTableRow.getRowNumber(), columnNumber + 1);
        String valueInThePast = getCellAdress(dataTableRow.getRowNumber(), columnNumber);
        String formula;
        if (0.0d == sheet.getRow(dataTableRow.getRowNumber()).getCell(columnNumber).getNumericCellValue()) {
            formula = "0";
        } else {
            formula = String.format(DYNAMIC_FORMULA, valueNow, valueInThePast);
        }
        headerCell.setCellFormula(formula);
        double value = headerCell.getNumericCellValue();
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
        headerCell.setCellStyle(cellStyle);
        createConditionFormattings(dynamicTableRow, columnNumber);
    }

    private void createConditionFormattings(DynamicTableRows dynamicTableRow, int columnNumber) {
        createConditionFormatting(dynamicTableRow.getRowNumber(), ComparisonOperator.GT, dynamicTableRow.backgroundColorIfPositive(), columnNumber);
        createConditionFormatting(dynamicTableRow.getRowNumber(), ComparisonOperator.LT, dynamicTableRow.backgroundColorIfNegative(), columnNumber);
        createConditionFormatting(dynamicTableRow.getRowNumber(), ComparisonOperator.EQUAL, IndexedColors.GREY_25_PERCENT.getIndex(), columnNumber);
    }

    private void createConditionFormatting(int rowNumber, byte operation, short color, int columnNumber) {
        XSSFSheetConditionalFormatting conditionalFormatting = sheet.getSheetConditionalFormatting();
        XSSFConditionalFormattingRule formattingRule = conditionalFormatting.createConditionalFormattingRule(operation,"0");
        XSSFPatternFormatting patternFormatting = formattingRule.createPatternFormatting();
        patternFormatting.setFillBackgroundColor(color);
        String firstCellAddress = getCellAdress(rowNumber, 1);
        String lastCellAdress = getCellAdress(rowNumber, columnNumber);
        CellRangeAddress[] dataRange = {CellRangeAddress.valueOf(String.format("%s:%s", firstCellAddress, lastCellAdress))};
        conditionalFormatting.addConditionalFormatting(dataRange, formattingRule);
    }

    private String getCellAdress(int row, int collumn) {
        Cell cell = sheet.getRow(row).getCell(collumn);
        return cell.getAddress().formatAsString();
    }
}
