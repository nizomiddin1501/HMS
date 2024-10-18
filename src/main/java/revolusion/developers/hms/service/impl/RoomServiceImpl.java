package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.RoomCategory;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.repository.RoomCategoryRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.RoomService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final HotelRepository hotelRepository;




    @Override
    public Page<RoomDto> getAllRooms(int page, int size) {
        Page<Room> roomsPage = roomRepository.findAll(PageRequest.of(page, size));
        return roomsPage.map(this::roomToDto);
    }

    @Override
    public Optional<RoomDto> getRoomById(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));
        RoomDto roomDto = roomToDto(room);
        return Optional.ofNullable(roomDto);
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) throws RoomException {
        Room room = dtoToRoom(roomDto);
        if (room.getRoomNumber() == null || roomDto.getRoomCategoryDto() == null) {
            throw new RoomException("Room number and category cannot be null");
        }
        boolean exists = roomRepository.existsByRoomNumber(room.getRoomNumber());
        if (exists) {
            throw new RoomException("Room with this number already exists");
        }
        RoomCategory existingCategory = roomCategoryRepository.findById(roomDto.getRoomCategoryDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomDto.getRoomCategoryDto().getId()));

        Hotel hotel = hotelRepository.findById(roomDto.getHotelDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", roomDto.getHotelDto().getId()));

        room.setRoomCategory(existingCategory);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        return roomToDto(savedRoom);
    }

    @Override
    public RoomDto updateRoom(Long roomId, RoomDto roomDto) throws ResourceNotFoundException {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));

        existingRoom.setRoomNumber(roomDto.getRoomNumber());
        RoomCategory existingCategory = roomCategoryRepository.findById(roomDto.getRoomCategoryDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomDto.getRoomCategoryDto().getId()));

        Hotel hotel = hotelRepository.findById(roomDto.getHotelDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "Id", roomDto.getHotelDto().getId()));

        existingRoom.setRoomCategory(existingCategory);
        existingRoom.setHotel(hotel);

        Room updatedRoom = roomRepository.save(existingRoom);
        return roomToDto(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));
        roomRepository.delete(room);
    }


    private Room dtoToRoom(RoomDto roomDto) {
        return modelMapper.map(roomDto, Room.class);
    }
    public RoomDto roomToDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }


}
