package ru.akhitev.megaplan.progress.daily.report.rows;

import org.apache.poi.ss.usermodel.IndexedColors;

public enum DynamicTableRows implements ReportRow {
    TOOK_IN_WORK(13, "Взято в работу", IndexedColors.WHITE, IndexedColors.WHITE),
    OUR_REFUGES(14, "Отказ (наш)", IndexedColors.RED, IndexedColors.BRIGHT_GREEN),
    ALL_CAUSES_RESERVE(15, "Резерв (все причины)", IndexedColors.RED, IndexedColors.BRIGHT_GREEN),
    LAUNCHED_IN_WORK(16, "Выведено в работу", IndexedColors.BRIGHT_GREEN, IndexedColors.RED),
    CANDIDATE_REFUGES(17, "Отказ кандидата", IndexedColors.RED, IndexedColors.BRIGHT_GREEN),
    LAUNCHES_AVERAGE_TERM(18, "Средний срок вывода", IndexedColors.RED, IndexedColors.BRIGHT_GREEN),
    LAUNCH_PORTION(19, "Доля вывода", IndexedColors.BRIGHT_GREEN, IndexedColors.RED),
    REFUGES_PORTION(20, "Доля отказа", IndexedColors.RED, IndexedColors.BRIGHT_GREEN),
    QUALITY_INDEX(21, "Индекс качества", IndexedColors.BRIGHT_GREEN, IndexedColors.RED);

    private int rowNumber;
    private String rowHeader;
    private IndexedColors backgroundColorIfPositive;
    private IndexedColors backgroundColorIfNegative;

    DynamicTableRows(int rowNumber, String rowHeader, IndexedColors backgroundColorIfPositive, IndexedColors backgroundColorIfNegative) {
        this.rowNumber = rowNumber;
        this.rowHeader = rowHeader;
        this.backgroundColorIfPositive = backgroundColorIfPositive;
        this.backgroundColorIfNegative = backgroundColorIfNegative;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getRowHeader() {
        return rowHeader;
    }

    public short backgroundColorIfPositive() {
        return backgroundColorIfPositive.index;
    }

    public short backgroundColorIfNegative() {
        return backgroundColorIfNegative.index;
    }
}
