package revolusion.developers.hms.service.impl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoleDto;
import revolusion.developers.hms.repository.RoleRepository;
import revolusion.developers.hms.service.RoleService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;



    @Override
    public Page<RoleDto> getAllRoles(int page, int size) {
        Page<Role> rolesPage = roleRepository.findAll(PageRequest.of(page, size));
        return rolesPage.map(this::roleToDto);
    }

    @Override
    public Optional<RoleDto> getRoleById(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        RoleDto roleDto = roleToDto(role);
        return Optional.ofNullable(roleDto);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) throws RoleException {
        Role role = dtoToRole(roleDto);
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new RoomException("Role name cannot be null or empty");
        }
        boolean exists = roleRepository.existsByName(role.getName());
        if (exists) {
            throw new RoleException("Role with this name already exists");
        }
        Role savedRole = roleRepository.save(role);
        return roleToDto(savedRole);
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) throws ResourceNotFoundException {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        existingRole.setName(roleDto.getName());
        Role updatedRole = roleRepository.save(existingRole);
        return roleToDto(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        roleRepository.delete(role);
    }


    private Role dtoToRole(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }
    public RoleDto roleToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }


}
