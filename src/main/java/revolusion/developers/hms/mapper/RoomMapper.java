package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.payload.RoomDto;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "roomCategoryDto", source = "roomCategory")
    @Mapping(target = "hotelDto", source = "hotel")
    RoomDto roomToDto(Room room);

    @Mapping(target = "roomCategory", source = "roomCategoryDto")
    @Mapping(target = "hotel", source = "hotelDto")
    Room dtoToRoom(RoomDto roomDto);

}
