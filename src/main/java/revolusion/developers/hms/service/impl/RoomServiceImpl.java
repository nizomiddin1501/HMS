package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.repository.RoomRepository;

import revolusion.developers.hms.service.RoomService;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Optional<RoomDto> getRoomById(Long roomId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) throws RoomException {
        return null;
    }

    @Override
    public RoomDto updateRoom(Long roomId, RoomDto roomDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) throws ResourceNotFoundException {

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
