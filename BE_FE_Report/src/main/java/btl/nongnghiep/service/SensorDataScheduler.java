package btl.nongnghiep.service.scheduler;

import btl.nongnghiep.repository.SensorDataRepository;
import btl.nongnghiep.service.SensorDataReportSyncService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataScheduler {

    private final SensorDataReportSyncService syncService;
    private final SensorDataRepository sensorDataRepository;

    public SensorDataScheduler(SensorDataReportSyncService syncService,
            SensorDataRepository sensorDataRepository) {
        this.syncService = syncService;
        this.sensorDataRepository = sensorDataRepository;
    }

    /**
     * Chạy mỗi 1 phút
     */
    @Scheduled(fixedRate = 60000)
    public void syncAllSensors() {

        List<String> sensorIds = sensorDataRepository.findAllSensorIds();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(1);

        for (String sensorId : sensorIds) {
            syncService.syncHourlyReports(sensorId, start, now);
        }
    }
}