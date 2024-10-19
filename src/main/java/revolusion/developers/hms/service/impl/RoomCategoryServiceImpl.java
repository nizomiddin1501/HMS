package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.RoomCategory;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomCategoryException;
import revolusion.developers.hms.mapper.RoomCategoryMapper;
import revolusion.developers.hms.payload.RoomCategoryDto;
import revolusion.developers.hms.repository.RoomCategoryRepository;
import revolusion.developers.hms.service.RoomCategoryService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomCategoryServiceImpl implements RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomCategoryMapper roomCategoryMapper;



    @Override
    public Page<RoomCategoryDto> getAllRoomCategories(int page, int size) {
        Page<RoomCategory> roomCategoryPages = roomCategoryRepository.findAll(PageRequest.of(page, size));
        return roomCategoryPages.map(roomCategoryMapper::roomCategoryToDto);
    }

    @Override
    public Optional<RoomCategoryDto> getRoomCategoryById(Long roomCategoryId) throws ResourceNotFoundException {
        RoomCategory roomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));
        return Optional.of(roomCategoryMapper.roomCategoryToDto(roomCategory));
    }

    @Override
    public RoomCategoryDto createRoomCategory(RoomCategoryDto roomCategoryDto) throws RoomCategoryException {
        RoomCategory roomCategory = roomCategoryMapper.dtoToRoomCategory(roomCategoryDto);
        if (roomCategory.getCategoryName() == null){
            throw new RoomCategoryException("Room category name must not be null");
        }
        boolean exists = roomCategoryRepository.existsByRoomCategoryName(roomCategory.getCategoryName());
        if (exists) {
            throw new RoomCategoryException("Room category with this categoryName already exists");
        }
        RoomCategory savedRoomCategory = roomCategoryRepository.save(roomCategory);
        return roomCategoryMapper.roomCategoryToDto(savedRoomCategory);
    }

    @Override
    public RoomCategoryDto updateRoomCategory(Long roomCategoryId, RoomCategoryDto roomCategoryDto) throws ResourceNotFoundException {
        RoomCategory existingRoomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));
        RoomCategory roomCategoryDetails = roomCategoryMapper.dtoToRoomCategory(roomCategoryDto);
        existingRoomCategory.setCategoryName(roomCategoryDetails.getCategoryName());
        existingRoomCategory.setPrice(roomCategoryDetails.getPrice());
        RoomCategory updatedRoomCategory = roomCategoryRepository.save(existingRoomCategory);
        return roomCategoryMapper.roomCategoryToDto(updatedRoomCategory);
    }

    @Override
    public void deleteRoomCategory(Long roomCategoryId) throws ResourceNotFoundException {
        RoomCategory roomCategory = roomCategoryRepository.findById(roomCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomCategoryId));
        roomCategoryRepository.delete(roomCategory);
    }
}
