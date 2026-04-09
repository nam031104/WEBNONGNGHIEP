package btl.nongnghiep.repository;

import btl.nongnghiep.entity.SensorData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SensorDataRepository extends JpaRepository<SensorData, String> {

  @Query("select distinct sd.idSensor from SensorData sd order by sd.idSensor")
  List<String> findAllSensorIds();

  Optional<SensorData> findFirstByOrderByIdSensor();

  List<SensorData> findByIdSensorOrderByCreatedAtDesc(String sensorId, Pageable pageable);

  @Query("""
      select sd
      from SensorData sd
      where sd.idSensor = :sensorId
        and sd.createdAt >= :startDateTime
        and sd.createdAt < :endDateTime
      """)
  List<SensorData> findSensorDataByRange(@Param("sensorId") String sensorId,
      @Param("startDateTime") LocalDateTime startDateTime,
      @Param("endDateTime") LocalDateTime endDateTime);

  @Query("""
          select sd.unit
          from SensorData sd
          where sd.idSensor = :sensorId
          order by sd.createdAt desc
      """)
  List<String> findLatestUnit(@Param("sensorId") String sensorId, Pageable pageable);

  default String findLatestUnitBySensorId(String sensorId) {
    List<String> units = findLatestUnit(sensorId, Pageable.ofSize(1));
    return units.isEmpty() ? "" : units.get(0);
  }
}
