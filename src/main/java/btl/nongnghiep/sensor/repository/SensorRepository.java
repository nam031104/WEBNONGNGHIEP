package btl.nongnghiep.sensor.repository;

import btl.nongnghiep.sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
