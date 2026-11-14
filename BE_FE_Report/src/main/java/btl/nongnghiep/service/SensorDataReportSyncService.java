package btl.nongnghiep.service;

import java.time.LocalDateTime;

public interface SensorDataReportSyncService {

    void syncHourlyReports(String sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
