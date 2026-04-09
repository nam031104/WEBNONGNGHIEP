package btl.nongnghiep.dto;

import java.util.List;

public class SensorChartViewDto {

    private final String sensorId;
    private final String chartTitle;
    private final String chartType;
    private final String unit;
    private final String selectedPeriodLabel;
    private final List<SensorChartPointDto> points;
    private final LatestAverageDto latestAverage;
    private final long totalRecords;

    public SensorChartViewDto(String sensorId,
                              String chartTitle,
                              String chartType,
                              String unit,
                              String selectedPeriodLabel,
                              List<SensorChartPointDto> points,
                              LatestAverageDto latestAverage,
                              long totalRecords) {
        this.sensorId = sensorId;
        this.chartTitle = chartTitle;
        this.chartType = chartType;
        this.unit = unit;
        this.selectedPeriodLabel = selectedPeriodLabel;
        this.points = points;
        this.latestAverage = latestAverage;
        this.totalRecords = totalRecords;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public String getChartType() {
        return chartType;
    }

    public String getUnit() {
        return unit;
    }

    public String getSelectedPeriodLabel() {
        return selectedPeriodLabel;
    }

    public List<SensorChartPointDto> getPoints() {
        return points;
    }

    public LatestAverageDto getLatestAverage() {
        return latestAverage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public List<String> getLabels() {
        return points.stream().map(SensorChartPointDto::getLabel).toList();
    }

    public List<Double> getValues() {
        return points.stream().map(SensorChartPointDto::getValue).toList();
    }
}
