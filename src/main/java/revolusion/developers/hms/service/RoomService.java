package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Page<RoomDto> getAllRooms(int page, int size);

    Optional<RoomDto> getRoomById(Long roomId);

    RoomDto createRoom(RoomDto roomDto);

    RoomDto updateRoom(Long roomId, RoomDto roomDto);

    void deleteRoom(Long roomId);
}
