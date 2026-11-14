package btl.nongnghiep.report.service;

import btl.nongnghiep.report.dto.SensorChartViewDto;

import java.time.LocalDate;
import java.time.YearMonth;

public interface SensorChartService {

    SensorChartViewDto buildDailyChart(String sensorId, LocalDate selectedDate);

    SensorChartViewDto buildWeeklyChart(String sensorId, LocalDate selectedDate);

    SensorChartViewDto buildMonthlyChart(String sensorId, YearMonth selectedMonth);
}
