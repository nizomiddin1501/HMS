package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.payload.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {


    Page<RoleDto> getAllRoles(int page, int size);

    Optional<RoleDto> getRoleById(Long roleId);

    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(Long roleId, RoleDto roleDto);

    void deleteRole(Long roleId);


}
