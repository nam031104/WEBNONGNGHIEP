package btl.nongnghiep.Device.Repository;


import btl.nongnghiep.Device.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    @Query("select d from Device d where d.idUser = :id")
     List<Device> findByUserId(@Param("id") String id);
}
