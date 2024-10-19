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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;


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
    public UserDto createUser(UserDto userDto) throws UserException {
        User user = userMapper.dtoToUser(userDto);
        if (user.getName() == null ||
                user.getName().isEmpty() ||
                user.getEmail() == null ||
                user.getEmail().isEmpty()) {
            throw new UserException("User name and email must not be null or empty");
        }
        boolean exists = userRepository.existsByEmail(user.getEmail());
        if (exists) {
            throw new UserException("User with this email already exists");
        }
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setIsVerified(false);

//        if (userDto.getPassword() != null) {
//            String hashedPassword = passwordEncoder.encode(userDto.getPassword());
//            user.setPassword(hashedPassword);
//        }

        User savedUser = userRepository.save(user);
        String toEmail = savedUser.getEmail();
        String subject = "Email Verification";
        String body = "Dear " + savedUser.getName() + ",\n\n" +
                "Please verify your email using the following code: " + verificationCode +
                "\n\nThank you!";
        emailService.sendEmail(toEmail, subject, body);
        return userMapper.userToDto(savedUser);
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
        String resetCode = generateVerificationCode();
        user.setResetCode(resetCode);
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
        user.setPassword(newPassword);
        user.setResetCode(null);
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


    public String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }



}
