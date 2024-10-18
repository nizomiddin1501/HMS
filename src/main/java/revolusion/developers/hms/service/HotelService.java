package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.HotelDto;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Page<HotelDto> getAllHotels(int page, int size);

    Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException;

    HotelDto createHotel(HotelDto hotelDto) throws HotelException;

    HotelDto updateHotel(Long hotelId, HotelDto hotelDto) throws ResourceNotFoundException;

    void deleteHotel(Long hotelId) throws ResourceNotFoundException;






}
