package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.service.HotelService;

import java.util.Optional;

/**
 * Controller for handling requests related to Hotel operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting hotel information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;


    /**
     * Retrieve a paginated list of hotels.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of hotels per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated HotelDto list
     */
    @Operation(summary = "Get all Hotels with Pagination", description = "Retrieve a paginated list of all hotels.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of hotels.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<HotelDto>>> getAllHotels(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<HotelDto> hotelDtos = hotelService.getAllHotels(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of hotels.",
                true,
                hotelDtos), HttpStatus.OK);
    }


    /**
     * Retrieve a hotel by their unique ID using the provided HotelDto.
     *
     * @param id the ID of the hotel to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the HotelDto and
     * an HTTP status of OK
     */
    @Operation(summary = "Get Hotel by ID", description = "Retrieve a hotel by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the hotel.")
    @ApiResponse(responseCode = "404", description = "Hotel not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<HotelDto>> getHotelById(@PathVariable Long id) {
        HotelDto hotelDto = hotelService.getHotelById(id)
                .orElseThrow(() -> new OrderException("Hotel not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the hotel.",
                true,
                hotelDto), HttpStatus.OK);
        }


    /**
     * Creates a new hotel.
     *
     * @param hotelDto the DTO containing the hotel information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved hotel data
     */
    @Operation(summary = "Create a new Hotel", description = "Create a new hotel record.")
    @ApiResponse(responseCode = "201", description = "Hotel created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<HotelDto>> createHotel(@Valid @RequestBody HotelDto hotelDto) {
        HotelDto savedHotel = hotelService.createHotel(hotelDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Hotel created successfully",
                true,
                savedHotel), HttpStatus.CREATED);
    }


    /**
     * Update the details of an existing hotel using the provided HotelDto.
     *
     * @param id       the ID of the hotel to be updated
     * @param hotelDto the DTO containing updated hotel details
     * @return a ResponseEntity containing a CustomApiResponse with the updated HotelDto
     */
    @Operation(summary = "Update hotel", description = "Update the details of an existing hotel.")
    @ApiResponse(responseCode = "200", description = "Hotel updated successfully")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<HotelDto>> updateHotel(
            @PathVariable Long id,
            @RequestBody HotelDto hotelDto) {
        HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Hotel updated successfully",
                true,
                updatedHotel), HttpStatus.OK);
        }

    /**
     * Delete a hotel by their ID.
     *
     * @param id the ID of the hotel to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Hotel", description = "Delete a hotel by its ID.")
    @ApiResponse(responseCode = "204", description = "Hotel deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Hotel not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Hotel deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }
}
