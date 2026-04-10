package btl.nongnghiep.sensor.service;

import btl.nongnghiep.sensor.entity.Sensor;
import btl.nongnghiep.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public String getTypeSensorById(String idSensor) {
        Optional<Sensor> sensor = sensorRepository.findById(idSensor);
        if (sensor.isPresent()) {
            return sensor.get().getTypeSensor();
        } else {
            throw new RuntimeException("Device not found");
        }

    }
}
