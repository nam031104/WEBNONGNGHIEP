package btl.nongnghiep.service.impl;

import btl.nongnghiep.entity.SensorData;
import btl.nongnghiep.entity.SensorDataReport;
import btl.nongnghiep.repository.SensorDataReportRepository;
import btl.nongnghiep.repository.SensorDataRepository;
import btl.nongnghiep.service.SensorDataReportSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.DoubleSummaryStatistics;

@Service
public class SensorDataReportSyncServiceImpl implements SensorDataReportSyncService {

    private static final String HOUR_PERIOD_TYPE = "HOUR";

    private final SensorDataRepository sensorDataRepository;
    private final SensorDataReportRepository sensorDataReportRepository;

    public SensorDataReportSyncServiceImpl(SensorDataRepository sensorDataRepository,
            SensorDataReportRepository sensorDataReportRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorDataReportRepository = sensorDataReportRepository;
    }

    @Override
    @Transactional
    public void syncHourlyReports(String sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // Lấy toàn bộ dữ liệu raw trong khoảng thời gian
        List<SensorData> sourceData = sensorDataRepository.findSensorDataByRange(sensorId, startDateTime, endDateTime);

        // Bucket dữ liệu theo giờ
        Map<LocalDateTime, List<SensorData>> hourlyBuckets = new LinkedHashMap<>();

        for (SensorData data : sourceData) {

            if (data.getCreatedAt() == null || data.getValue() == null) {
                continue;
            }

            LocalDateTime hourKey = data.getCreatedAt()
                    .truncatedTo(ChronoUnit.HOURS);

            hourlyBuckets
                    .computeIfAbsent(hourKey, key -> new java.util.ArrayList<>())
                    .add(data);
        }

        // Tính toán và lưu report
        for (Map.Entry<LocalDateTime, List<SensorData>> entry : hourlyBuckets.entrySet()) {

            List<SensorData> hourlyData = entry.getValue();

            if (hourlyData.isEmpty())
                continue;

            DoubleSummaryStatistics stats = hourlyData.stream()
                    .mapToDouble(SensorData::getValue)
                    .summaryStatistics();

            double avg = stats.getAverage();
            double min = stats.getMin();
            double max = stats.getMax();

            String unit = hourlyData.stream()
                    .map(SensorData::getUnit)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("");

            SensorDataReport report = sensorDataReportRepository
                    .findByIdSensorAndPeriodTypeAndPeriodTime(
                            sensorId,
                            HOUR_PERIOD_TYPE,
                            entry.getKey())
                    .orElseGet(SensorDataReport::new);

            report.setIdSensor(sensorId);
            report.setPeriodType(HOUR_PERIOD_TYPE);
            report.setPeriodTime(entry.getKey());
            report.setAvgValue((float) avg);
            report.setMinValue((float) min);
            report.setMaxValue((float) max);
            report.setUnit(unit);

            if (report.getCreatedAt() == null) {
                report.setCreatedAt(LocalDateTime.now());
            }

            sensorDataReportRepository.save(report);
        }
    }
}