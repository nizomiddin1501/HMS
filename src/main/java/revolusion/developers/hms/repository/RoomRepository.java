package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.Room;

public interface RoomRepository extends BaseRepository<Room, Long> {


    // Room Number exists check
    @Query(value = "select count(*) > 0 from room r where r.room_number = :roomNumber", nativeQuery = true)
    boolean existsByRoomNumber(@Param("roomNumber") String roomNumber);







}
