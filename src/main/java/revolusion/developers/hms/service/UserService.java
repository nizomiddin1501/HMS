package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {


    Page<UserDto> getAllUsers(int page, int size);

    Optional<UserDto> getUserById(Long userId) throws ResourceNotFoundException;

    void createUser(UserDto userDto) throws UserException;

    void verifyUser(String email, String verificationCode) throws UserException;

    void sendResetPasswordEmail(String email) throws UserException;

    void resetPassword(String email, String resetCode, String newPassword) throws UserException;

    UserDto updateUser(Long userId, UserDto userDto) throws ResourceNotFoundException;

    void deleteUser(Long userId) throws ResourceNotFoundException;
}
