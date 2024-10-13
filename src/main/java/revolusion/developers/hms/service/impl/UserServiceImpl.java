package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.repository.UserRepository;
import revolusion.developers.hms.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }


    @Override
    public List<UserDto> getAllUsers(int page, int size) {
        List<User> users = userRepository.findAll(PageRequest.of(page, size)).getContent();
        return users.stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id", userId));

        // Convert User entity to UserDto
        UserDto userDto = userToDto(user);
        return Optional.ofNullable(userDto);
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserException {
        return null;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        // Conversion DTO to entity
        User userDetails = dtoToUser(userDto);

        // update user details
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setAbout(userDetails.getAbout());

        // Save updated user
        User updatedUser = userRepository.save(existingUser);

        // Convert updated user entity to DTO and return
        return userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
        userRepository.delete(user);
    }

    // DTO ---> Entity
    private User dtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    // Entity ---> DTO
    public UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }



}
