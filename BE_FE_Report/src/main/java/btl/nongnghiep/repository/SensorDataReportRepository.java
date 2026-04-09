package btl.nongnghiep.repository;

import btl.nongnghiep.entity.SensorDataReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SensorDataReportRepository extends JpaRepository<SensorDataReport, Long> {

    Optional<SensorDataReport> findByIdSensorAndPeriodTypeAndPeriodTime(String idSensor,
                                                                        String periodType,
                                                                        LocalDateTime periodTime);

    @Query("""
            select sdr
            from SensorDataReport sdr
            where sdr.idSensor = :sensorId
              and sdr.periodType = :periodType
              and sdr.periodTime >= :startDateTime
              and sdr.periodTime < :endDateTime
            order by sdr.periodTime asc
            """)
    List<SensorDataReport> findReportsByPeriod(@Param("sensorId") String sensorId,
                                               @Param("periodType") String periodType,
                                               @Param("startDateTime") LocalDateTime startDateTime,
                                               @Param("endDateTime") LocalDateTime endDateTime);
}
