package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.mapper.HotelMapper;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.service.HotelService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;


    @Override
    public Page<HotelDto> getAllHotels(int page, int size) {
        Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, size));
        return hotelsPage.map(hotelMapper::hotelToDto);
    }

    @Override
    public Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));
        return Optional.of(hotelMapper.hotelToDto(hotel));
    }

    @Override
    public HotelDto createHotel(HotelDto hotelDto) throws HotelException {
        Hotel hotel = hotelMapper.dtoToHotel(hotelDto);
        if (hotel.getName() == null || hotel.getAddress() == null) {
            throw new HotelException("Hotel name and address columns cannot be null");
        }
        boolean exists = hotelRepository.existsByName(hotel.getName());
        if (exists) {
            throw new HotelException("Hotel with this name already exists");
        }
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.hotelToDto(savedHotel);
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
        return hotelMapper.hotelToDto(updatedHotel);
    }

    @Override
    public void deleteHotel(Long hotelId) throws ResourceNotFoundException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", " Id ", hotelId));
        hotelRepository.delete(hotel);
    }
}
