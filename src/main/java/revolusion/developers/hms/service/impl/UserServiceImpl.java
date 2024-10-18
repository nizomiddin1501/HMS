package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.repository.UserRepository;
import revolusion.developers.hms.service.EmailService;
import revolusion.developers.hms.service.UserService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;


    @Override
    public Page<UserDto> getAllUsers(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size));
        return usersPage.map(this::userToDto);
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id", userId));

        UserDto userDto = userToDto(user);
        return Optional.ofNullable(userDto);
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserException {
        // 1. Convert DTO to entity
        User user = dtoToUser(userDto);

        // 2. Perform business checks on the entity
        if (user.getName() == null ||
                user.getName().isEmpty() ||
                user.getEmail() == null ||
                user.getEmail().isEmpty()) {
            throw new UserException("User name and email must not be null or empty");
        }

        // 3. Checking that the email column does not exist
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if (exists) {
            throw new UserException("User with this email already exists");
        }

        // 5. Generate verification code
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setIsVerified(false);


//        if (userDto.getPassword() != null) {
//            String hashedPassword = passwordEncoder.encode(userDto.getPassword());
//            user.setPassword(hashedPassword);
//        }

        // 4. Save User
        User savedUser = userRepository.save(user);

        // 6. Send confirmation email after successful registration
        String toEmail = savedUser.getEmail();
        String subject = "Email Verification";
        String body = "Dear " + savedUser.getName() + ",\n\n" +
                "Please verify your email using the following code: " + verificationCode +
                "\n\nThank you!";
        emailService.sendEmail(toEmail, subject, body);

        // 7. Convert the saved User to DTO and return
        return userToDto(savedUser);
    }


    @Override
    public void verifyUser(String email, String verificationCode) throws UserException {
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
                throw  new UserException("User not found");
        }
        if (user.isVerified()) {
            throw new UserException("User is already verified");
        }

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new UserException("Invalid verification code");
        }

        String storedVerificationCode = user.getVerificationCode();
        if (storedVerificationCode == null || !storedVerificationCode.equals(verificationCode)) {
            throw new UserException("Invalid verification code");
        }

        user.setIsVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }


    @Override
    public void sendResetPasswordEmail(String email ) throws UserException {
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            throw new UserException("User with this email does not exist");
        }

        // 1. Generate reset code
        String resetCode = generateVerificationCode();
        user.setResetCode(resetCode);
        userRepository.save(user);

        // 2. Send email with reset code
        String subject = "Password Reset Request";
        String body = "Dear " + user.getName() + ",\n\n" +
                "To reset your password, please use the following code: " + resetCode +
                "\n\nThank you!";
        emailService.sendEmail(email, subject, body);
    }

    @Override
    public void resetPassword(String email, String resetCode, String newPassword) throws UserException {
        User user = userRepository.findByUserEmail(email);
        if (user == null || !user.getResetCode().equals(resetCode)) {
            throw new UserException("Invalid email or reset code");
        }

        user.setPassword(newPassword);
        user.setResetCode(null);
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws ResourceNotFoundException {
        // 1. Get the available user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        // 2. update user details
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(userDto.getPassword());
        }
        existingUser.setAbout(userDto.getAbout());

        // 3. Save updated user
        User updatedUser = userRepository.save(existingUser);

        // 4. Convert updated user entity to DTO and return
        return userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
        userRepository.delete(user);
    }


    public String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

    private User dtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }


    public UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }


}
