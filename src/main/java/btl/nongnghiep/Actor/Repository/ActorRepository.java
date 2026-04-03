package btl.nongnghiep.Actor.Repository;

import btl.nongnghiep.Actor.Entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, String> {
}
