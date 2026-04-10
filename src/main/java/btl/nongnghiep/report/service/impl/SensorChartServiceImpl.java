package btl.nongnghiep.report.service.impl;

import btl.nongnghiep.report.dto.SensorChartPointDto;
import btl.nongnghiep.report.dto.SensorChartViewDto;
import btl.nongnghiep.report.entity.SensorDataReport;
import btl.nongnghiep.report.repository.SensorDataReportRepository;
import btl.nongnghiep.report.service.SensorChartService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class SensorChartServiceImpl implements SensorChartService {

    private static final Locale ENGLISH = Locale.ENGLISH;
    private static final String HOUR_PERIOD_TYPE = "HOUR";

    private final SensorDataReportRepository sensorDataReportRepository;

    public SensorChartServiceImpl(SensorDataReportRepository sensorDataReportRepository) {
        this.sensorDataReportRepository = sensorDataReportRepository;
    }

    // ===================== DAILY =====================
    @Override
    public SensorChartViewDto buildDailyChart(String sensorId, LocalDate selectedDate) {

        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<SensorDataReport> reportData =
                sensorDataReportRepository.findReportsByPeriod(sensorId, HOUR_PERIOD_TYPE, start, end);

        Map<String, List<SensorDataReport>> bucketMap = new LinkedHashMap<>();

        for (int hour = 0; hour < 24; hour++) {
            bucketMap.put(String.format("%02d:00", hour), new ArrayList<>());
        }

        for (SensorDataReport data : reportData) {
            if (data.getPeriodTime() == null) continue;

            String label = String.format("%02d:00", data.getPeriodTime().getHour());
            bucketMap.get(label).add(data);
        }

        return buildChartView(
                sensorId,
                "Daily Sensor Monitoring",
                "line",
                selectedDate.toString(),
                reportData,
                bucketMap
        );
    }

    // ===================== WEEKLY =====================
    @Override
    public SensorChartViewDto buildWeeklyChart(String sensorId, LocalDate selectedDate) {

        LocalDate weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(7);

        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekEnd.atStartOfDay();

        List<SensorDataReport> reportData =
                sensorDataReportRepository.findReportsByPeriod(sensorId, HOUR_PERIOD_TYPE, start, end);

        Map<String, List<SensorDataReport>> bucketMap = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate day = weekStart.plusDays(i);
            String label = day.getDayOfWeek().getDisplayName(TextStyle.FULL, ENGLISH);
            bucketMap.put(label, new ArrayList<>());
        }

        for (SensorDataReport data : reportData) {
            if (data.getPeriodTime() == null) continue;

            String label = data.getPeriodTime()
                    .getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, ENGLISH);

            bucketMap.get(label).add(data);
        }

        return buildChartView(
                sensorId,
                "Weekly Sensor Monitoring",
                "line",
                weekStart + " to " + weekStart.plusDays(6),
                reportData,
                bucketMap
        );
    }

    // ===================== MONTHLY =====================
    @Override
    public SensorChartViewDto buildMonthlyChart(String sensorId, YearMonth selectedMonth) {

        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.plusMonths(1).atDay(1);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();

        List<SensorDataReport> reportData =
                sensorDataReportRepository.findReportsByPeriod(sensorId, HOUR_PERIOD_TYPE, start, end);

        Map<String, List<SensorDataReport>> bucketMap = new LinkedHashMap<>();

        for (int day = 1; day <= selectedMonth.lengthOfMonth(); day++) {
            bucketMap.put("Day " + day, new ArrayList<>());
        }

        for (SensorDataReport data : reportData) {
            if (data.getPeriodTime() == null) continue;

            String label = "Day " + data.getPeriodTime().getDayOfMonth();
            bucketMap.get(label).add(data);
        }

        return buildChartView(
                sensorId,
                "Monthly Sensor Monitoring",
                "bar",
                selectedMonth.toString(),
                reportData,
                bucketMap
        );
    }

    // ===================== COMMON BUILDER =====================
    private SensorChartViewDto buildChartView(String sensorId,
                                              String chartTitle,
                                              String chartType,
                                              String selectedPeriodLabel,
                                              List<SensorDataReport> rangeData,
                                              Map<String, List<SensorDataReport>> bucketMap) {

        List<SensorChartPointDto> points = new ArrayList<>();

        for (Map.Entry<String, List<SensorDataReport>> entry : bucketMap.entrySet()) {

            DoubleSummaryStatistics stats = entry.getValue().stream()
                    .map(SensorDataReport::getAvgValue)
                    .filter(Objects::nonNull)
                    .mapToDouble(Float::doubleValue)
                    .summaryStatistics();

            double avg = stats.getAverage();

            points.add(new SensorChartPointDto(entry.getKey(), avg));
        }

        // ✅ lấy unit từ report
        String unit = rangeData.stream()
                .map(SensorDataReport::getUnit)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");

        return new SensorChartViewDto(
                sensorId,
                chartTitle,
                chartType,
                unit,
                selectedPeriodLabel,
                points,
                rangeData.size()
        );
    }
}