package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends BaseRepository<Order, Long> {



    // Get the warrants for the room
    @Query(value = "select * from orders o where o.room_id = :roomId", nativeQuery = true)
    List<Order> findByRoomId(@Param("roomId") Long roomId);

    // get all order from not deadline
    List<Order> findAllByDeadlineBefore(LocalDateTime deadline);

    // get all order by hotelId
    @Query(value = "select * from orders o where o.room_id in (select r.id from room r where r.hotel_id = :hotelId)", nativeQuery = true)
    List<Order> findByHotelId(@Param("hotelId") Long hotelId);

}
