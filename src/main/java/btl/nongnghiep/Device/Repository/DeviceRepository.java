package btl.nongnghiep.Device.Repository;


import btl.nongnghiep.Device.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device,String> {
}
