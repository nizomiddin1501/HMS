package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomCategoryException;
import revolusion.developers.hms.payload.RoomCategoryDto;

import java.util.List;
import java.util.Optional;

public interface RoomCategoryService {


    // get all roomCategories using pagination
    Page<RoomCategoryDto> getAllRoomCategories(int page, int size);

    // get roomCategory by ID
    Optional<RoomCategoryDto> getRoomCategoryById(Long roomCategoryId) throws ResourceNotFoundException;

    // create a new roomCategory
    RoomCategoryDto createRoomCategory(RoomCategoryDto roomCategoryDto) throws RoomCategoryException;

    // update an existing roomCategory
    RoomCategoryDto updateRoomCategory(Long roomCategoryId, RoomCategoryDto roomCategoryDto) throws ResourceNotFoundException;

    // delete a roomCategory
    void deleteRoomCategory(Long roomCategoryId) throws ResourceNotFoundException;




}
