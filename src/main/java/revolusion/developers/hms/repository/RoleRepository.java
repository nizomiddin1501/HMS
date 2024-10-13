package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.Role;

public interface RoleRepository extends BaseRepository<Role,Long>{



    // Role name exists check
    @Query(value = "select count(*) > 0 from role r where r.role_name = :roleName", nativeQuery = true)
    boolean existsByName(@Param("roleName") String roleName);


}
