package ru.akhitev.megaplan.progress.daily.report;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

class XlsChartCreator {
    private XSSFSheet sheet;
    private int dataColumnCount;

    XlsChartCreator(XSSFSheet sheet, int dataColumnCount) {
        this.sheet = sheet;
        this.dataColumnCount = dataColumnCount;
    }

    void drawGraphic(XlsReportRows xlsReportRow, int leftTopCornerOfChart) {
        XSSFChart chart = prepareChartBase(leftTopCornerOfChart);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Дата");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(xlsReportRow.getRowHeader());
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 1, 1, dataColumnCount));
        XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(xlsReportRow.getRowNumber(), xlsReportRow.getRowNumber(), 1, dataColumnCount));
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(xs, ys1);
        series1.setSmooth(false);
        series1.setMarkerStyle(MarkerStyle.STAR);

        chart.plot(data);

        solidLineSeries(data, 0, PresetColor.RED);
    }

    private XSSFChart prepareChartBase(int leftTopCornerOfChart) {
        int rightBottomCornerOfChart = leftTopCornerOfChart + 9;
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(leftTopCornerOfChart, 0, rightBottomCornerOfChart, 10, 0, leftTopCornerOfChart, 10, rightBottomCornerOfChart);
        return drawing.createChart(anchor);
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }
}
