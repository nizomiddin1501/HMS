package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.RoomCategory;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomCategoryException;
import revolusion.developers.hms.payload.RoomCategoryDto;
import revolusion.developers.hms.repository.RoomCategoryRepository;
import revolusion.developers.hms.service.RoomCategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomCategoryServiceImpl implements RoomCategoryService {

    private final ModelMapper modelMapper;

    private final RoomCategoryRepository roomCategoryRepository;


    @Autowired
    public RoomCategoryServiceImpl(
            ModelMapper modelMapper,
            RoomCategoryRepository roomCategoryRepository) {
        this.modelMapper = modelMapper;
        this.roomCategoryRepository = roomCategoryRepository;
    }


    @Override
    public List<RoomCategoryDto> getAllRoomCategories(int page, int size) {
        List<RoomCategory> roomCategories = roomCategoryRepository.findAll(PageRequest.of(page, size)).getContent();
        return roomCategories.stream()
                .map(this::roomCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomCategoryDto> getRoomCategoryById(Long roomCategoryId) throws ResourceNotFoundException {
        RoomCategory roomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));

        // Convert RoomCategory entity to RoomCategoryDto
        RoomCategoryDto roomCategoryDto = roomCategoryToDto(roomCategory);
        return Optional.ofNullable(roomCategoryDto);
    }

    @Override
    public RoomCategoryDto createRoomCategory(RoomCategoryDto roomCategoryDto) throws RoomCategoryException {
        // 1. Convert DTO to entity
        RoomCategory roomCategory = dtoToRoomCategory(roomCategoryDto);

        // 2. Perform business checks on the entity
        if (roomCategory.getCategoryName() == null){
            throw new RoomCategoryException("Room category name must not be null");
        }

        // 3. Checking that the room category name column do not exist
        boolean exists = roomCategoryRepository.existsByRoomCategoryName(roomCategory.getCategoryName());
        if (exists) {
            throw new RoomCategoryException("Room category with this categoryName already exists");
        }

        // 4. Save RoomCategory
        RoomCategory savedRoomCategory = roomCategoryRepository.save(roomCategory);

        // 5. Convert the saved RoomCategory to DTO and return
        return roomCategoryToDto(savedRoomCategory);
    }

    @Override
    public RoomCategoryDto updateRoomCategory(Long roomCategoryId, RoomCategoryDto roomCategoryDto) throws ResourceNotFoundException {
        // 1. Get the available roomCategory
        RoomCategory existingRoomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));

        // 2. Convert DTO to entity
        RoomCategory roomCategoryDetails = dtoToRoomCategory(roomCategoryDto);

        // 3. update roomCategory details
        existingRoomCategory.setCategoryName(roomCategoryDetails.getCategoryName());
        existingRoomCategory.setPrice(roomCategoryDetails.getPrice());

        // 4. Save updated roomCategory
        RoomCategory updatedRoomCategory = roomCategoryRepository.save(existingRoomCategory);

        // 5. Convert updated user entity to DTO and return
        return roomCategoryToDto(updatedRoomCategory);
    }

    @Override
    public void deleteRoomCategory(Long roomCategoryId) throws ResourceNotFoundException {
        RoomCategory roomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));
        roomCategoryRepository.delete(roomCategory);
    }

    // DTO ---> Entity
    private RoomCategory dtoToRoomCategory(RoomCategoryDto roomCategoryDto) {
        return modelMapper.map(roomCategoryDto, RoomCategory.class);
    }

    // Entity ---> DTO
    public RoomCategoryDto roomCategoryToDto(RoomCategory roomCategory) {
        return modelMapper.map(roomCategory, RoomCategoryDto.class);
    }

}
