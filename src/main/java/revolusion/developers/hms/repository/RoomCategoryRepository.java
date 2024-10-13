package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.RoomCategory;

public interface RoomCategoryRepository extends BaseRepository<RoomCategory,Long> {



    // Room Category Name exists check
    @Query(value = "select count(*) > 0 from room_category r where r.category_name = :categoryName", nativeQuery = true)
    boolean existsByRoomCategoryName(@Param("categoryName") String categoryName);



}
