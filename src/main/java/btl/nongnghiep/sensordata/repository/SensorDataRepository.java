package btl.nongnghiep.sensordata.repository;

import btl.nongnghiep.sensordata.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData,String> {
}
