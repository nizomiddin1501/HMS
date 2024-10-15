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
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.payload.UserPaymentDto;
import revolusion.developers.hms.repository.UserPaymentRepository;
import revolusion.developers.hms.service.UserPaymentService;

import java.util.Optional;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    private final ModelMapper modelMapper;
    private final UserPaymentRepository userPaymentRepository;


    @Autowired
    public UserPaymentServiceImpl(ModelMapper modelMapper, UserPaymentRepository userPaymentRepository) {
        this.modelMapper = modelMapper;
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
        return null;
    }


    // update methodini ham bir kurib chiq
    @Override
    public UserPaymentDto updateUserPayment(Long userPaymentId, UserPaymentDto userPaymentDto) throws ResourceNotFoundException {
        UserPayment existingUserPayment = userPaymentRepository.findById(userPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPayment", " Id ", userPaymentId));

        // Conversion DTO to entity
        UserPayment userPaymentDetails = dtoToUserPayment(userPaymentDto);

        // update userPayment details
        existingUserPayment.setBalance(userPaymentDetails.getBalance());
        existingUserPayment.setAccountNumber(userPaymentDetails.getAccountNumber());

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
