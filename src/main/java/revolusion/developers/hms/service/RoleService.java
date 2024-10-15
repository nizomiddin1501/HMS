package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.payload.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {




    // get all roles using pagination
    Page<RoleDto> getAllRoles(int page, int size);

    // get role by ID
    Optional<RoleDto> getRoleById(Long roleId) throws ResourceNotFoundException;

    // create a new role
    RoleDto createRole(RoleDto roleDto) throws RoleException;

    // update an existing role
    RoleDto updateRole(Long roleId, RoleDto roleDto) throws ResourceNotFoundException;

    // delete a role
    void deleteRole(Long roleId) throws ResourceNotFoundException;


}
