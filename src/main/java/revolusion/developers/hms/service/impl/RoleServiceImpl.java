package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoleDto;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.RoleRepository;
import revolusion.developers.hms.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final ModelMapper modelMapper;

    private final RoleRepository roleRepository;


    @Autowired
    public RoleServiceImpl(
            ModelMapper modelMapper,
            RoleRepository roleRepository) {
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<RoleDto> getAllRoles(int page, int size) {
        List<Role> roles = roleRepository.findAll(PageRequest.of(page, size)).getContent();
        return roles.stream()
                .map(this::roleToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> getRoleById(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));

        // Convert Role entity to RoleDto
        RoleDto roleDto = roleToDto(role);
        return Optional.ofNullable(roleDto);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) throws RoleException {
        // 1. Convert DTO to Entity
        Role role = dtoToRole(roleDto);

        // 2. Perform business checks on the entity
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new RoomException("Role name cannot be null or empty");
        }

        // 3. Checking that the role name column does not exist
        boolean exists = roleRepository.existsByName(role.getName());
        if (exists) {
            throw new RoleException("Role with this name already exists");
        }

        // 4. Save Role
        Role savedRole = roleRepository.save(role);

        // 5. Convert the saved Role to DTO and return
        return roleToDto(savedRole);
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) throws ResourceNotFoundException {
        // 1. Get the available role
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));

        // 2. update user details
        existingRole.setName(roleDto.getName());

        // 3. Save updated role
        Role updatedRole = roleRepository.save(existingRole);

        // 4. Convert updated role entity to DTO and return
        return roleToDto(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) throws ResourceNotFoundException {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", " Id ", roleId));
        roleRepository.delete(role);
    }

    // DTO ---> Entity
    private Role dtoToRole(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

    // Entity ---> DTO
    public RoleDto roleToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }


}
