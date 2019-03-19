package ru.akhitev.megaplan.progress.daily.report.rows;

public enum DataTableRows implements ReportRow {
    TOOK_IN_WORK(2, "Взято в работу"),
    OUR_REFUGES(3, "Отказ (наш)"),
    ALL_CAUSES_RESERVE(4, "Резерв (все причины)"),
    LAUNCHED_IN_WORK(5, "Выведено в работу"),
    CANDIDATE_REFUGES(6, "Отказ кандидата"),
    LAUNCHES_AVERAGE_TERM(7, "Средний срок вывода"),
    LAUNCH_PORTION(8, "Доля вывода"),
    REFUGES_PORTION(9, "Доля отказа"),
    QUALITY_INDEX(10, "Индекс качества");

    private int rowNumber;
    private String rowHeader;

    DataTableRows(int rowNumber, String rowHeader) {
        this.rowNumber = rowNumber;
        this.rowHeader = rowHeader;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getRowHeader() {
        return rowHeader;
    }
}
