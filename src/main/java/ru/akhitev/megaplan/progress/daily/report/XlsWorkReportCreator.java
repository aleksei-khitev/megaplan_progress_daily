package ru.akhitev.megaplan.progress.daily.report;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.akhitev.megaplan.progress.daily.entity.Progress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class XlsWorkReportCreator {
    private XSSFWorkbook workbook;

    public void makeReport(Map<String, List<Progress>> allProgresses, String fileName) throws IOException {
        workbook = new XSSFWorkbook();
        allProgresses.forEach((key, value) -> {
            XlsSheetCreator sheetCreator = new XlsSheetCreator(value, workbook, key);
            sheetCreator.prepareSheet();
        });
        writeAndCloseBook(fileName);
    }

    private void writeAndCloseBook(String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        workbook.close();
    }
}
