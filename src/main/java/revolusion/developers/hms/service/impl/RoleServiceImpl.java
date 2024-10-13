package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.payload.RoleDto;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.RoleRepository;
import revolusion.developers.hms.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final ModelMapper modelMapper;

    private final RoleRepository roleRepository;


    @Autowired
    public RoleServiceImpl(ModelMapper modelMapper, RoleRepository roleRepository) {
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<RoleDto> getAllRoles(int page, int size) {
        return null;
    }

    @Override
    public Optional<RoleDto> getRoleById(Long roleId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) throws RoleException {
        return null;
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto roleDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteRole(Long roleId) throws ResourceNotFoundException {

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
