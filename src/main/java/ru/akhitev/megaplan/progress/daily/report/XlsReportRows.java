package ru.akhitev.megaplan.progress.daily.report;

import org.apache.poi.ss.usermodel.*;

enum XlsReportRows {
    TOOK_IN_WORK(2, "Взято в работу"),
    OUR_REFUGES(3, "Отказ (наш)"),
    ALL_CAUSES_RESERVE(4, "Резерв (все причины)"),
    LOW_RESERVE(5, "Резерв (низкие)"),
    LAUNCHED_IN_WORK(6, "Выведено в работу"),
    CANDIDATE_REFUGES(7, "Отказ кандидата"),
    LAUNCHES_AVERAGE_TERM(8, "Средний срок вывода"),
    LAUNCH_PORTION(9, "Доля вывода"),
    REFUGES_PORTION(10, "Доля отказа"),
    QUALITY_INDEX(11, "Индекс качества");

    private int rowNumber;
    private String rowHeader;

    XlsReportRows(int rowNumber, String rowHeader) {
        this.rowNumber = rowNumber;
        this.rowHeader = rowHeader;
    }

    void prepareHeaderCell(Sheet sheet, CellStyle headerCellStyle) {
        Cell headerCell = sheet.createRow(rowNumber).createCell(0);
        headerCell.setCellStyle(headerCellStyle);
        headerCell.setCellValue(rowHeader);
    }

    void writeIntegerData(Sheet sheet, Workbook workbook, int columnNumber, Integer value) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Cell headerCell = sheet.getRow(rowNumber).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    void writeDoubleData(Sheet sheet, Workbook workbook, int columnNumber, double value) {
        CellStyle cellStyle = prepareDataCellStyleBase(workbook);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("##.##"));
        Cell headerCell = sheet.getRow(rowNumber).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    void writePercentageData(Sheet sheet, Workbook workbook, int columnNumber, double value) {
        CellStyle cellStyle = prepareDataCellStyleBase(workbook);
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
        Cell headerCell = sheet.getRow(rowNumber).createCell(columnNumber);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    private CellStyle prepareDataCellStyleBase(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    int getRowNumber() {
        return rowNumber;
    }

    String getRowHeader() {
        return rowHeader;
    }
}
