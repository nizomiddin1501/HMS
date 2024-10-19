package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.mapper.UserMapper;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.repository.UserRepository;
import revolusion.developers.hms.service.EmailService;
import revolusion.developers.hms.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    private ConcurrentHashMap<String, User> temporaryUsers = new ConcurrentHashMap<>();


    @Override
    public Page<UserDto> getAllUsers(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size));
        return usersPage.map(userMapper::userToDto);
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id", userId));
        return Optional.of(userMapper.userToDto(user));
    }

    @Override
    public void createUser(UserDto userDto) throws UserException {
        if (userDto.getName() == null || userDto.getEmail() == null) {
            throw new UserException("User name and email must not be null or empty");
        }
        boolean exists = userRepository.existsByEmail(userDto.getEmail());
        if (exists) {
            throw new UserException("User with this email already exists");
        }
        String verificationCode = generateVerificationCode();

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setVerificationCode(verificationCode);
        user.setIsVerified(false);

        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(1));

        temporaryUsers.put(user.getEmail(), user);

        String toEmail = user.getEmail();
        String subject = "Email Verification";
        String body = "Dear " + user.getName() + ",\n\n" +
                "Please verify your email using the following code: " + verificationCode +
                "\n\nThank you!";
        emailService.sendEmail(toEmail, subject, body);
    }

    @Override
    public void verifyUser(String email, String verificationCode) throws UserException {
        User user = temporaryUsers.get(email);
        if (user == null) {
                throw  new UserException("User not found");
        }
        if (user.isVerified()) {
            throw new UserException("User is already verified");
        }

        if (user.getCodeExpiryTime() != null && user.getCodeExpiryTime().isBefore(LocalDateTime.now())) {
            throw new UserException("Verification code has expired. Please request a new one.");
        }

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new UserException("Invalid verification code");
        }

        user.setIsVerified(true);
        temporaryUsers.remove(email);
        userRepository.save(user);
    }


    @Override
    public void sendResetPasswordEmail(String email ) throws UserException {
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            throw new UserException("User with this email does not exist");
        }
        String resetCode = generateVerificationCode();
        user.setResetCode(resetCode);

        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(1));

        userRepository.save(user);
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
        if (user.getCodeExpiryTime() != null && user.getCodeExpiryTime().isBefore(LocalDateTime.now())) {
            throw new UserException("Reset code has expired. Please request a new one.");
        }

        // Hash the new password before saving
//        user.setPassword(passwordEncoder.encode(newPassword));

        user.setPassword(newPassword);
        user.setResetCode(null);
        user.setCodeExpiryTime(null);
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(userDto.getPassword());
        }
        existingUser.setAbout(userDto.getAbout());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
        userRepository.delete(user);
    }


    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 10000));
    }

}
