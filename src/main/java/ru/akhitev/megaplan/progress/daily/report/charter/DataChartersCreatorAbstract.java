package ru.akhitev.megaplan.progress.daily.report.charter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.akhitev.megaplan.progress.daily.report.rows.DataTableRows;

public class DataChartersCreatorAbstract extends AbstractXlsChartCreator {
    private int startRow;

    public DataChartersCreatorAbstract(XSSFSheet sheet, int startRow, int dataColumnCount) {
        super(sheet, 0, dataColumnCount);
        this.startRow = startRow;
    }

    public void create() {
        drawGraphic(DataTableRows.LAUNCHES_AVERAGE_TERM,currentRow(0));
        drawGraphic(DataTableRows.LAUNCH_PORTION, currentRow(1));
        drawGraphic(DataTableRows.LAUNCHED_IN_WORK, currentRow(2));
        drawGraphic(DataTableRows.ALL_CAUSES_RESERVE, currentRow(3));
        drawGraphic(DataTableRows.REFUGES_PORTION, currentRow(4));
        drawGraphic(DataTableRows.CANDIDATE_REFUGES, currentRow(5));
        drawGraphic(DataTableRows.TOOK_IN_WORK, currentRow(6));
        drawGraphic(DataTableRows.OUR_REFUGES, currentRow(7));
    }

    private int currentRow(int step) {
        return startRow + step * 10;
    }
}
