package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.User;

public interface UserRepository extends BaseRepository<User, Long> {


    // Name exists check
    @Query(value = "select count(*) > 0 from users u where u.name = :name", nativeQuery = true)
    boolean existsByName(@Param("name") String name);

    // Email exists check
    @Query(value = "select count(*) > 0 from users u where u.email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);









}
