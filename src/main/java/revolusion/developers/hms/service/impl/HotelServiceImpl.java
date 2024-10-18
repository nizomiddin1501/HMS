package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.service.HotelService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;


    @Override
    public Page<HotelDto> getAllHotels(int page, int size) {
        Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, size));
        return hotelsPage.map(this::hotelToDto);
    }

    @Override
    public Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));
        HotelDto hotelDto = hotelToDto(hotel);
        return Optional.ofNullable(hotelDto);
    }

    @Override
    public HotelDto createHotel(HotelDto hotelDto) throws HotelException {
        Hotel hotel = dtoToHotel(hotelDto);
        if (hotel.getName() == null || hotel.getAddress() == null) {
            throw new HotelException("Hotel name and address columns cannot be null");
        }
        boolean exists = hotelRepository.existsByName(hotel.getName());
        if (exists) {
            throw new HotelException("Hotel with this name already exists");
        }
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelToDto(savedHotel);
    }

    @Override
    public HotelDto updateHotel(Long hotelId, HotelDto hotelDto) throws ResourceNotFoundException {
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));

        existingHotel.setName(hotelDto.getName());
        existingHotel.setAddress(hotelDto.getAddress());
        existingHotel.setStarRating(hotelDto.getStarRating());
        existingHotel.setDescription(hotelDto.getDescription());
        Hotel updatedHotel = hotelRepository.save(existingHotel);
        return hotelToDto(updatedHotel);
    }

    @Override
    public void deleteHotel(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));
        hotelRepository.delete(hotel);
    }


    private Hotel dtoToHotel(HotelDto hotelDto) {
        return modelMapper.map(hotelDto, Hotel.class);
    }
    public HotelDto hotelToDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

}
