package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.service.HotelService;
import java.util.Optional;

/**
 * Controller for handling requests related to Hotel operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting hotel information.
 */
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    /**
     * Constructor for HotelController.
     *
     * @param hotelService the service to manage hotel records
     * @Autowired automatically injects the HotelService bean
     */
    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }




    /**
     * Retrieve a paginated list of all hotels.
     *
     * This method fetches a paginated list of hotel records and returns them as a list of HotelDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of hotels per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of HotelDto representing all hotels
     */
    @Operation(summary = "Get all Hotels with Pagination", description = "Retrieve a paginated list of all hotels.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of hotels.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<HotelDto>>> getAllHotels(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<HotelDto> hotelDtos = hotelService.getAllHotels(page,size);
        CustomApiResponse<Page<HotelDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of hotels.",
                true,
                hotelDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Retrieve a hotel by their unique ID using the provided HotelDto.
     *
     * This method retrieves a hotel's details based on their ID and returns
     * a CustomApiResponse containing the corresponding HotelDto if found.
     * If the hotel does not exist, it returns a CustomApiResponse with a
     * message indicating that the hotel was not found and a 404 Not Found status.
     *
     * @param id the ID of the hotel to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the HotelDto and
     *         an HTTP status of OK, or a NOT FOUND status if the role does not exist.
     */
    @Operation(summary = "Get Hotel by ID", description = "Retrieve a hotel by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the hotel.")
    @ApiResponse(responseCode = "404", description = "Hotel not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<HotelDto>> getHotelById(@PathVariable Long id) {
        Optional<HotelDto> hotelDto = hotelService.getHotelById(id);
        if (hotelDto.isPresent()){
            CustomApiResponse<HotelDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the hotel.",
                    true,
                    hotelDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<HotelDto> response = new CustomApiResponse<>(
                    "Hotel not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new hotel.
     *
     * This method validates the incoming hotel data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param hotelDto the DTO containing the role information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved hotel data
     */
    @Operation(summary = "Create a new Hotel", description = "Create a new hotel record.")
    @ApiResponse(responseCode = "201", description = "Hotel created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<HotelDto>> createHotel(@Valid @RequestBody HotelDto hotelDto){
        HotelDto savedHotel = hotelService.createHotel(hotelDto);
        CustomApiResponse<HotelDto> response = new CustomApiResponse<>(
                "Hotel created successfully",
                true,
                savedHotel
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing hotel using the provided HotelDto.
     *
     * This method accepts the hotel's ID and a DTO containing updated hotel details.
     * It updates the hotel record if it exists and returns the updated HotelDto object.
     *
     * @param id the ID of the role to be updated
     * @param hotelDto the DTO containing updated hotel details
     * @return a ResponseEntity containing a CustomApiResponse with the updated HotelDto,
     *         or a NOT FOUND response if the hotel does not exist
     */
    @Operation(summary = "Update hotel", description = "Update the details of an existing hotel.")
    @ApiResponse(responseCode = "200", description = "Hotel updated successfully")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<HotelDto>>  updateHotel(
            @PathVariable Long id,
            @RequestBody HotelDto hotelDto) {
        Optional<HotelDto> hotelDtoOptional = hotelService.getHotelById(id);
        if (hotelDtoOptional.isPresent()) {
            HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
            CustomApiResponse<HotelDto> response = new CustomApiResponse<>(
                    "Hotel updated successfully",
                    true,
                    updatedHotel
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<HotelDto> response = new CustomApiResponse<>(
                    "Hotel not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a hotel by their ID.
     *
     * This method deletes the hotel record based on the given ID if it exists.
     *
     * @param id the ID of the hotel to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the hotel does not exist
     */
    @Operation(summary = "Delete Hotel", description = "Delete a hotel by its ID.")
    @ApiResponse(responseCode = "204", description = "Hotel deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Hotel not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        Optional<HotelDto> hotelDto = hotelService.getHotelById(id);
        if (hotelDto.isPresent()) {
            hotelService.deleteHotel(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Hotel deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Hotel not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }


}
