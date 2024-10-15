package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {


    // get all users using pagination
    Page<UserDto> getAllUsers(int page, int size);

    // get user by ID
    Optional<UserDto> getUserById(Long userId) throws ResourceNotFoundException;

    // create a new user
    UserDto createUser(UserDto userDto) throws UserException;

    // update an existing user
    UserDto updateUser(Long userId, UserDto userDto) throws ResourceNotFoundException;

    // delete a user
    void deleteUser(Long userId) throws ResourceNotFoundException;
}
