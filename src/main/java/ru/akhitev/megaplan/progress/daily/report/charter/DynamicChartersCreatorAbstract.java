package ru.akhitev.megaplan.progress.daily.report.charter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.akhitev.megaplan.progress.daily.report.rows.DataTableRows;
import ru.akhitev.megaplan.progress.daily.report.rows.DynamicTableRows;

public class DynamicChartersCreatorAbstract extends AbstractXlsChartCreator {
    private int startRow;

    public DynamicChartersCreatorAbstract(XSSFSheet sheet, int startRow, int dataColumnCount) {
        super(sheet, 8, dataColumnCount);
        this.startRow = startRow;
    }

    public void create() {
        drawGraphic(DynamicTableRows.LAUNCHES_AVERAGE_TERM,currentRow(0));
        drawGraphic(DynamicTableRows.LAUNCH_PORTION, currentRow(1));
        drawGraphic(DynamicTableRows.LAUNCHED_IN_WORK, currentRow(2));
        drawGraphic(DynamicTableRows.ALL_CAUSES_RESERVE, currentRow(3));
        drawGraphic(DynamicTableRows.REFUGES_PORTION, currentRow(4));
        drawGraphic(DynamicTableRows.CANDIDATE_REFUGES, currentRow(5));
        drawGraphic(DynamicTableRows.TOOK_IN_WORK, currentRow(6));
        drawGraphic(DynamicTableRows.OUR_REFUGES, currentRow(7));
    }

    private int currentRow(int step) {
        return startRow + step * 10;
    }
}
