package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.payload.UserDto;
@Mapper(componentModel = "spring",
        uses = {RoleMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserDto userToDto(User user);

    @Mapping(target = "roles", source = "roles")
    User dtoToUser(UserDto userDto);

}
