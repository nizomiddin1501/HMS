package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.payload.OrderDto;

import java.util.List;

public interface HotelRepository extends BaseRepository<Hotel, Long>{



    // Hotel Name exists check
    @Query(value = "select count(*) > 0 from hotel h where h.hotel_name = :hotelName", nativeQuery = true)
    boolean existsByName(@Param("hotelName") String hotelName);



}
