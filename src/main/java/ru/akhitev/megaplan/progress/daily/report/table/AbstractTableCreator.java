package ru.akhitev.megaplan.progress.daily.report.table;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.akhitev.megaplan.progress.daily.entity.Progress;
import ru.akhitev.megaplan.progress.daily.report.rows.ReportRow;

import java.util.List;

abstract class AbstractTableCreator {
    static final String DATE_TEMPLATE = "dd.MM";
    String employeeName;
    List<Progress> progresses;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Font rowHeaderFont;
    Font dateHeaderFont;
    Font dataFont;

    AbstractTableCreator(String employeeName, List<Progress> progresses, XSSFWorkbook workbook, XSSFSheet sheet) {
        this.employeeName = employeeName;
        this.progresses = progresses;
        this.workbook = workbook;
        this.sheet = sheet;
        rowHeaderFont = prepareRowHeaderFont();
        dateHeaderFont = prepareDateHeaderFont();
        dataFont = prepareDataFont();
    }

    void prepareHeaderCell(Sheet sheet, ReportRow reportRow, CellStyle headerCellStyle) {
        drawBorders(headerCellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.createRow(reportRow.getRowNumber()).createCell(0);
        headerCell.setCellStyle(headerCellStyle);
        headerCell.setCellValue(reportRow.getRowHeader());
    }

    void writeIntegerData(Sheet sheet, Workbook workbook, Font font, ReportRow reportRow, int columnNumber, Integer value) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        drawBorders(cellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.getRow(reportRow.getRowNumber()).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    void writeDoubleData(Sheet sheet, Workbook workbook, Font font, ReportRow reportRow, int columnNumber, double value) {
        CellStyle cellStyle = prepareDataCellStyleBase(workbook, font);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00"));
        drawBorders(cellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.getRow(reportRow.getRowNumber()).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    void writePercentageData(Sheet sheet, Workbook workbook, Font font, ReportRow reportRow, int columnNumber, double value) {
        CellStyle cellStyle = prepareDataCellStyleBase(workbook, font);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
        drawBorders(cellStyle, BorderStyle.THIN);
        Cell headerCell = sheet.getRow(reportRow.getRowNumber()).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    private CellStyle prepareDataCellStyleBase(Workbook workbook, Font font) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    private Font prepareRowHeaderFont() {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    private Font prepareDateHeaderFont() {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    private Font prepareDataFont() {
        Font font = workbook.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    protected void drawBorders(CellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
    }
}
