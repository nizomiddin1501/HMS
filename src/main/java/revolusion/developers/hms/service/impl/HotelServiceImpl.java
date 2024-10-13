package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.service.HotelService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {


    private final ModelMapper modelMapper;

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(ModelMapper modelMapper, HotelRepository hotelRepository) {
        this.modelMapper = modelMapper;
        this.hotelRepository = hotelRepository;
    }


    @Override
    public List<HotelDto> getAllHotels(int page, int size) {
        List<Hotel> hotels = hotelRepository.findAll(PageRequest.of(page, size)).getContent();
        return hotels.stream()
                .map(this::hotelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));

        // Convert Hotel entity to HotelDto
        HotelDto hotelDto = hotelToDto(hotel);
        return Optional.ofNullable(hotelDto);
    }

    @Override
    public HotelDto createHotel(HotelDto hotelDto) throws HotelException {
        // 1. Convert DTO to Entity
        Hotel hotel = dtoToHotel(hotelDto);

        // 2. Perform business checks on the entity
        if (hotel.getName() == null || hotel.getAddress() == null) {
            throw new HotelException("Hotel name and address columns cannot be null");
        }

        // 3. Checking that the name column does not exist
        boolean exists = hotelRepository.existsByName(hotel.getName());
        if (exists) {
            throw new HotelException("Hotel with this name already exists");
        }

        // 4. Save Hotel
        Hotel savedHotel = hotelRepository.save(hotel);

        // 5. Convert the saved Hotel to DTO and return
        return hotelToDto(savedHotel);
    }

    @Override
    public HotelDto updateHotel(Long hotelId, HotelDto hotelDto) throws ResourceNotFoundException {
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));

        // update hotel details
        existingHotel.setName(hotelDto.getName());
        existingHotel.setAddress(hotelDto.getAddress());
        existingHotel.setStarRating(hotelDto.getStarRating());
        existingHotel.setDescription(hotelDto.getDescription());

        // Save updated hotel
        Hotel updatedHotel = hotelRepository.save(existingHotel);

        // Convert updated hotel entity to DTO and return
        return hotelToDto(updatedHotel);
    }

    @Override
    public void deleteHotel(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));
        hotelRepository.delete(hotel);
    }

    // DTO ---> Entity
    private Hotel dtoToHotel(HotelDto hotelDto) {
        return modelMapper.map(hotelDto, Hotel.class);
    }

    // Entity ---> DTO
    public HotelDto hotelToDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

}
