package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
public class UserPaymentServiceImpl implements UserPaymentService {

    private final ModelMapper modelMapper;
    private final UserPaymentRepository userPaymentRepository;
    private final UserRepository userRepository;



    @Override
    public Page<UserPaymentDto> getAllUserPayments(int page, int size) {
        Page<UserPayment> userPaymentsPage = userPaymentRepository.findAll(PageRequest.of(page, size));
        return userPaymentsPage.map(this::userPaymentToDto);
    }

    @Override
    public Optional<UserPaymentDto> getUserPaymentById(Long userPaymentId) throws ResourceNotFoundException {
        UserPayment userPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id", userPaymentId));
        UserPaymentDto userPaymentDto = userPaymentToDto(userPayment);
        return Optional.ofNullable(userPaymentDto);
    }

    @Override
    public UserPaymentDto createUserPayment(UserPaymentDto userPaymentDto) throws UserPaymentException {
        UserPayment userPayment = dtoToUserPayment(userPaymentDto);
        if (userPayment.getAccountNumber() == null || userPayment.getAccountNumber().isEmpty()) {
            throw new UserPaymentException("UserPayment accountNumber cannot be null or empty");
        }
        boolean exists = userPaymentRepository.existsByAccountNumber(userPayment.getAccountNumber());
        if (exists) {
            throw new UserPaymentException("UserPayment with this accountNumber already exists");
        }
        User existingUser = userRepository.findById(userPaymentDto.getUserDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userPaymentDto.getUserDto().getId()));

        userPayment.setUser(existingUser);
        UserPayment savedUserPayment = userPaymentRepository.save(userPayment);
        return userPaymentToDto(savedUserPayment);
    }


    // update methodini ham bir kurib chiq
    @Override
    public UserPaymentDto updateUserPayment(Long userPaymentId, UserPaymentDto userPaymentDto) throws ResourceNotFoundException {
        UserPayment existingUserPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id ", userPaymentId));
        existingUserPayment.setBalance(userPaymentDto.getBalance());
        existingUserPayment.setAccountNumber(userPaymentDto.getAccountNumber());
        UserPayment updatedUserPayment = userPaymentRepository.save(existingUserPayment);
        return userPaymentToDto(updatedUserPayment);
    }

    @Override
    public void deleteUserPayment(Long userPaymentId) throws ResourceNotFoundException {
        UserPayment userPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id ", userPaymentId));
        userPaymentRepository.delete(userPayment);
    }


    private UserPayment dtoToUserPayment(UserPaymentDto userPaymentDto) {
        return modelMapper.map(userPaymentDto, UserPayment.class);
    }
    public UserPaymentDto userPaymentToDto(UserPayment userPayment) {
        return modelMapper.map(userPayment, UserPaymentDto.class);
    }

}
