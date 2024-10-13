package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.RoomRepository;

import revolusion.developers.hms.service.RoomService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final ModelMapper modelMapper;

    private final RoomRepository roomRepository;


    @Autowired
    public RoomServiceImpl(ModelMapper modelMapper, RoomRepository roomRepository) {
        this.modelMapper = modelMapper;
        this.roomRepository = roomRepository;
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
        return null;
    }

    @Override
    public RoomDto updateRoom(Long roomId, RoomDto roomDto) throws ResourceNotFoundException {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", " Id ", roomId));

        // Conversion DTO to entity
        Room roomDetails = dtoToRoom(roomDto);

        // update room details
        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setCategory(roomDetails.getCategory());
        existingRoom.setPrice(roomDetails.getPrice());

        // Save updated room
        Room updatedRoom = roomRepository.save(existingRoom);

        // Convert updated room entity to DTO and return
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
