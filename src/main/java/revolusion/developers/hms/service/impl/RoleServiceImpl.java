package revolusion.developers.hms.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.mapper.RoleMapper;
import revolusion.developers.hms.payload.RoleDto;
import revolusion.developers.hms.repository.RoleRepository;
import revolusion.developers.hms.service.RoleService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Page<RoleDto> getAllRoles(int page, int size) {
        Page<Role> rolesPage = roleRepository.findAll(PageRequest.of(page, size));
        return rolesPage.map(roleMapper::roleToDto);
    }

    @Override
    public Optional<RoleDto> getRoleById(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        return Optional.of(roleMapper.roleToDto(role));
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) throws RoleException {
        Role role = roleMapper.dtoToRole(roleDto);
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new RoleException("Role name cannot be null or empty");
        }
        boolean exists = roleRepository.existsByName(role.getName());
        if (exists) {
            throw new RoleException("Role with this name already exists");
        }
        Role savedRole = roleRepository.save(role);
        return roleMapper.roleToDto(savedRole);
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) throws ResourceNotFoundException {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        existingRole.setName(roleDto.getName());
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.roleToDto(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        roleRepository.delete(role);
    }
}
