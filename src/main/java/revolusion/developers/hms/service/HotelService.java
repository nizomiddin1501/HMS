package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.HotelException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.HotelDto;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    // get all hotels using pagination
    Page<HotelDto> getAllHotels(int page, int size);

    // get hotel by ID
    Optional<HotelDto> getHotelById(Long hotelId) throws ResourceNotFoundException;

    // create a new hotel
    HotelDto createHotel(HotelDto hotelDto) throws HotelException;

    // update an existing hotel
    HotelDto updateHotel(Long hotelId, HotelDto hotelDto) throws ResourceNotFoundException;

    // delete a hotel
    void deleteHotel(Long hotelId) throws ResourceNotFoundException;






}
