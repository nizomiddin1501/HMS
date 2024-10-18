package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomCategoryException;
import revolusion.developers.hms.payload.RoomCategoryDto;

import java.util.List;
import java.util.Optional;

public interface RoomCategoryService {


    Page<RoomCategoryDto> getAllRoomCategories(int page, int size);

    Optional<RoomCategoryDto> getRoomCategoryById(Long roomCategoryId) throws ResourceNotFoundException;

    RoomCategoryDto createRoomCategory(RoomCategoryDto roomCategoryDto) throws RoomCategoryException;

    RoomCategoryDto updateRoomCategory(Long roomCategoryId, RoomCategoryDto roomCategoryDto) throws ResourceNotFoundException;

    void deleteRoomCategory(Long roomCategoryId) throws ResourceNotFoundException;




}
