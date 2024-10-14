package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.RoomCategory;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.repository.RoomCategoryRepository;
import revolusion.developers.hms.repository.RoomRepository;

import revolusion.developers.hms.service.RoomService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final ModelMapper modelMapper;

    private final RoomRepository roomRepository;

    private final RoomCategoryRepository roomCategoryRepository;

    private final HotelRepository hotelRepository;


    @Autowired
    public RoomServiceImpl(
            ModelMapper modelMapper,
            RoomRepository roomRepository,
            RoomCategoryRepository roomCategoryRepository,
            HotelRepository hotelRepository) {
        this.modelMapper = modelMapper;
        this.roomRepository = roomRepository;
        this.roomCategoryRepository = roomCategoryRepository;
        this.hotelRepository = hotelRepository;
    }


    @Override
    public List<RoomDto> getAllRooms(int page, int size) {
        List<Room> rooms = roomRepository.findAll(PageRequest.of(page, size)).getContent();
        return rooms.stream()
                .map(this::roomToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDto> getRoomById(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));

        // Convert Room entity to RoomDto
        RoomDto roomDto = roomToDto(room);
        return Optional.ofNullable(roomDto);
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) throws RoomException {
        // 1. Convert DTO to Entity
        Room room = dtoToRoom(roomDto);

        // 2. Perform business checks on the entity
        if (room.getRoomNumber() == null || roomDto.getRoomCategoryDto() == null) {
            throw new RoomException("Room number and category cannot be null");
        }

        // 3. Checking that the roomNumber column does not exist
        boolean exists = roomRepository.existsByRoomNumber(room.getRoomNumber());
        if (exists) {
            throw new RoomException("Room with this number already exists");
        }

        // 4. Get an existing RoomCategory and Hotel from repositories
        RoomCategory existingCategory = roomCategoryRepository.findById(roomDto.getRoomCategoryDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomDto.getRoomCategoryDto().getId()));

        Hotel hotel = hotelRepository.findById(roomDto.getHotelDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", roomDto.getHotelDto().getId()));

        // 4. Set the retrieved entities to the room
        room.setRoomCategory(existingCategory);
        room.setHotel(hotel);

        // 6. Save Room
        Room savedRoom = roomRepository.save(room);

        // 7. Convert the saved Room to DTO and return
        return roomToDto(savedRoom);
    }

    @Override
    public RoomDto updateRoom(Long roomId, RoomDto roomDto) throws ResourceNotFoundException {
        // 1. Get the available room
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));

        // 2. Update room details
        existingRoom.setRoomNumber(roomDto.getRoomNumber());

        // 3. Get an existing RoomCategory and Hotel from the repositories
        RoomCategory existingCategory = roomCategoryRepository.findById(roomDto.getRoomCategoryDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomCategory", " Id ", roomDto.getRoomCategoryDto().getId()));

        Hotel hotel = hotelRepository.findById(roomDto.getHotelDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "Id", roomDto.getHotelDto().getId()));

        // 4. Set the retrieved entities to the room
        existingRoom.setRoomCategory(existingCategory);
        existingRoom.setHotel(hotel);

        // 5. Save updated room
        Room updatedRoom = roomRepository.save(existingRoom);

        // 6. Convert updated room entity to DTO and return
        return roomToDto(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));
        roomRepository.delete(room);
    }

    // DTO ---> Entity
    private Room dtoToRoom(RoomDto roomDto) {
        return modelMapper.map(roomDto, Room.class);
    }

    // Entity ---> DTO
    public RoomDto roomToDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }


}
