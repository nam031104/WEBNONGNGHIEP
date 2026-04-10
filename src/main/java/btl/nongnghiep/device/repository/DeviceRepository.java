package btl.nongnghiep.device.repository;


import btl.nongnghiep.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    @Query("SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.actors WHERE d.idAccount = :idUser")
    List<Device> findByUserIdWithActors(@Param("idUser") String idUser);

    @Query("SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.sensors WHERE d.idAccount = :idUser")
    List<Device> findByUserIdWithSensors(@Param("idUser") String idUser);
}
