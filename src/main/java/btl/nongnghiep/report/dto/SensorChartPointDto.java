package btl.nongnghiep.report.dto;

public class SensorChartPointDto {

    private final String label;
    private final Double value;

    public SensorChartPointDto(String label, Double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Double getValue() {
        return value;
    }
}
