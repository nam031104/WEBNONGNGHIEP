package btl.nongnghiep.Sensor.Repository;

import btl.nongnghiep.Sensor.Entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
