package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.payload.HotelDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {


    HotelDto hotelToDto(Hotel hotel);

    Hotel dtoToHotel(HotelDto hotelDto);







}
