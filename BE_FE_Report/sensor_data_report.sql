CREATE TABLE sensor_data_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_sensor VARCHAR(255) NOT NULL,
    avg_value FLOAT,
    min_value FLOAT,
    max_value FLOAT,
    period_type VARCHAR(10),
    period_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_sensor) REFERENCES sensor(id_sensor)
        ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE sensor_data_report
ADD UNIQUE KEY uk_sensor_period (id_sensor, period_type, period_time),
ADD INDEX idx_sensor_period_time (id_sensor, period_type, period_time);
