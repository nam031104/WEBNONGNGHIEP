package btl.nongnghiep.schedule.repository;

import btl.nongnghiep.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    List<Schedule> findByIdActor(String idActor);

    @Query("SELECT s FROM Schedule s JOIN Actor a ON s.idActor = a.idActor JOIN Device d ON a.device.idDevice = d.idDevice WHERE d.idAccount = :idAccount")
    List<Schedule> findSchedulesByAccountId(@Param("idAccount") String idAccount);

    @Query("SELECT s FROM Schedule s WHERE s.date <= :now AND s.isExecuted = 0")
    List<Schedule> findPendingSchedules(@Param("now") LocalDateTime now);
}
