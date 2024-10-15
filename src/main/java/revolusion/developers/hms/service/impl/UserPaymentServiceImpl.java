package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.entity.UserPayment;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserPaymentException;
import revolusion.developers.hms.payload.UserPaymentDto;
import revolusion.developers.hms.repository.UserPaymentRepository;
import revolusion.developers.hms.repository.UserRepository;
import revolusion.developers.hms.service.UserPaymentService;

import java.util.Optional;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    private final ModelMapper modelMapper;
    private final UserPaymentRepository userPaymentRepository;
    private final UserRepository userRepository;


    @Autowired
    public UserPaymentServiceImpl(
            ModelMapper modelMapper,
            UserRepository userRepository,
            UserPaymentRepository userPaymentRepository
    ) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userPaymentRepository = userPaymentRepository;
    }


    @Override
    public Page<UserPaymentDto> getAllUserPayments(int page, int size) {
        Page<UserPayment> userPaymentsPage = userPaymentRepository.findAll(PageRequest.of(page, size));
        return userPaymentsPage.map(this::userPaymentToDto);
    }

    @Override
    public Optional<UserPaymentDto> getUserPaymentById(Long userPaymentId) throws ResourceNotFoundException {
        UserPayment userPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id", userPaymentId));

        // Convert UserPayment entity to UserPaymentDto
        UserPaymentDto userPaymentDto = userPaymentToDto(userPayment);
        return Optional.ofNullable(userPaymentDto);
    }

    @Override
    public UserPaymentDto createUserPayment(UserPaymentDto userPaymentDto) throws UserPaymentException {
        // 1. Convert DTO to Entity
        UserPayment userPayment = dtoToUserPayment(userPaymentDto);

        // 2. Perform business checks on the entity
        if (userPayment.getAccountNumber() == null || userPayment.getAccountNumber().isEmpty()) {
            throw new UserPaymentException("UserPayment accountNumber cannot be null or empty");
        }

        // 3. Checking that the userPayment accountNumber column does not exist
        boolean exists = userPaymentRepository.existsByAccountNumber(userPayment.getAccountNumber());
        if (exists) {
            throw new UserPaymentException("UserPayment with this accountNumber already exists");
        }

        // 4. Get an existing RoomCategory and Hotel from repositories
        User existingUser = userRepository.findById(userPaymentDto.getUserDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userPaymentDto.getUserDto().getId()));

        // 5. Set User to userPayment
        userPayment.setUser(existingUser);

        // 6. Save UserPayment
        UserPayment savedUserPayment = userPaymentRepository.save(userPayment);

        // 7. Convert the saved UserPayment to DTO and return
        return userPaymentToDto(savedUserPayment);
    }


    // update methodini ham bir kurib chiq
    @Override
    public UserPaymentDto updateUserPayment(Long userPaymentId, UserPaymentDto userPaymentDto) throws ResourceNotFoundException {
        UserPayment existingUserPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id ", userPaymentId));

        // update userPayment details
        existingUserPayment.setBalance(userPaymentDto.getBalance());
        existingUserPayment.setAccountNumber(userPaymentDto.getAccountNumber());

        // Save updated userPayment
        UserPayment updatedUserPayment = userPaymentRepository.save(existingUserPayment);

        // Convert updated userPayment entity to DTO and return
        return userPaymentToDto(updatedUserPayment);
    }

    @Override
    public void deleteUserPayment(Long userPaymentId) throws ResourceNotFoundException {
        UserPayment userPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id ", userPaymentId));
        userPaymentRepository.delete(userPayment);
    }

    // DTO ---> Entity
    private UserPayment dtoToUserPayment(UserPaymentDto userPaymentDto) {
        return modelMapper.map(userPaymentDto, UserPayment.class);
    }

    // Entity ---> DTO
    public UserPaymentDto userPaymentToDto(UserPayment userPayment) {
        return modelMapper.map(userPayment, UserPaymentDto.class);
    }

}
