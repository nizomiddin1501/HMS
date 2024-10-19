package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.RoomCategory;
import revolusion.developers.hms.payload.RoomCategoryDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomCategoryMapper {

    RoomCategoryDto roomCategoryToDto(RoomCategory roomCategory);

    RoomCategory dtoToRoomCategory(RoomCategoryDto roomCategoryDto);

}
