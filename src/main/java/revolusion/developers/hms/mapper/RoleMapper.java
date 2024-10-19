package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.payload.RoleDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "name", source = "name")
    RoleDto roleToDto(Role role);

    @Mapping(target = "name", source = "name")
    Role dtoToRole(RoleDto roleDto);

}
