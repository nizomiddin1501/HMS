package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Long> {


    // Email exists check
    @Query(value = "select count(*) > 0 from users u where u.user_email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);


    // Get user email
    @Query(value = "select * from users u where u.user_email = :email", nativeQuery = true)
    User findByUserEmail(@Param("email") String email);










}
