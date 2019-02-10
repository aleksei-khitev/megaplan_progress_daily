package ru.akhitev.megaplan.progress.daily.report.rows;

import org.apache.poi.ss.usermodel.IndexedColors;

public enum DynamicTableRows implements ReportRow {
    TOOK_IN_WORK(14, "Взято в работу", IndexedColors.WHITE, IndexedColors.WHITE),
    OUR_REFUGES(15, "Отказ (наш)", IndexedColors.RED, IndexedColors.GREEN),
    ALL_CAUSES_RESERVE(16, "Резерв (все причины)", IndexedColors.RED, IndexedColors.GREEN),
    LOW_RESERVE(17, "Резерв (низкие)", IndexedColors.RED, IndexedColors.GREEN),
    LAUNCHED_IN_WORK(18, "Выведено в работу", IndexedColors.GREEN, IndexedColors.RED),
    CANDIDATE_REFUGES(19, "Отказ кандидата", IndexedColors.RED, IndexedColors.GREEN),
    LAUNCHES_AVERAGE_TERM(20, "Средний срок вывода", IndexedColors.RED, IndexedColors.GREEN),
    LAUNCH_PORTION(21, "Доля вывода", IndexedColors.GREEN, IndexedColors.RED),
    REFUGES_PORTION(22, "Доля отказа", IndexedColors.RED, IndexedColors.GREEN),
    QUALITY_INDEX(23, "Индекс качества", IndexedColors.GREEN, IndexedColors.RED);

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
