package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Hotel;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.service.HotelService;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public HotelDto createHotel(HotelDto hotelDto) throws HotelException {
        return null;
    }

    @Override
    public HotelDto updateHotel(Long hotelId, HotelDto hotelDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteHotel(Long hotelId) throws ResourceNotFoundException {

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
