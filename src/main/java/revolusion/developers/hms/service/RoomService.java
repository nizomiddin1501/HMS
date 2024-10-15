package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    // get all rooms using pagination
    Page<RoomDto> getAllRooms(int page, int size);

    // get room by ID
    Optional<RoomDto> getRoomById(Long roomId) throws ResourceNotFoundException;

    // create a new room
    RoomDto createRoom(RoomDto roomDto) throws RoomException;

    // update an existing room
    RoomDto updateRoom(Long roomId, RoomDto roomDto) throws ResourceNotFoundException;

    // delete a room
    void deleteRoom(Long roomId) throws ResourceNotFoundException;
}
