package ru.akhitev.megaplan.progress.daily.report.rows;

public enum DataTableRows implements ReportRow {
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
