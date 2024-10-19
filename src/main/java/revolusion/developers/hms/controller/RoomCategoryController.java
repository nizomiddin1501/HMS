package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.RoomCategoryDto;
import revolusion.developers.hms.service.RoomCategoryService;
 import java.util.Optional;

/**
 * Controller for handling requests related to RoomCategory operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting roomCategory information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roomCategories")
public class RoomCategoryController {


    private final RoomCategoryService roomCategoryService;



    /**
     * Retrieve a paginated list of all roomCategories.
     *
     * This method fetches a paginated list of roomCategory records and returns them as a list of RoomCategoryDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of roomCategories per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of RoomCategoryDto representing all roomCategories
     */
    @Operation(summary = "Get all RoomCategories with Pagination", description = "Retrieve a paginated list of all roomCategories.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of roomCategories.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoomCategoryDto>>> getAllRoomCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<RoomCategoryDto> roomCategoryDtos = roomCategoryService.getAllRoomCategories(page,size);
        CustomApiResponse<Page<RoomCategoryDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of roomCategories.",
                true,
                roomCategoryDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    /**
     * Retrieve a roomCategory by their unique ID using the provided RoomCategoryDto.
     *
     * This method retrieves a roomCategory's details based on their ID and returns
     * a CustomApiResponse containing the corresponding RoomCategoryDto if found.
     * If the roomCategory does not exist, it returns a CustomApiResponse with a
     * message indicating that the roomCategory was not found and a 404 Not Found status.
     *
     * @param id the ID of the roomCategory to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the RoomCategoryDto and
     *         an HTTP status of OK, or a NOT FOUND status if the roomCategory does not exist.
     */
    @Operation(summary = "Get RoomCategory by ID", description = "Retrieve a roomCategory by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the roomCategory.")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>> getRoomCategoryById(@PathVariable Long id) {
        Optional<RoomCategoryDto> roomCategoryDto = roomCategoryService.getRoomCategoryById(id);
        if (roomCategoryDto.isPresent()){
            CustomApiResponse<RoomCategoryDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the roomCategory.",
                    true,
                    roomCategoryDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoomCategoryDto> response = new CustomApiResponse<>(
                    "RoomCategory not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new roomCategory.
     *
     * This method validates the incoming roomCategory data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param roomCategoryDto the DTO containing the roomCategory information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved roomCategory data
     */
    @Operation(summary = "Create a new RoomCategory", description = "Create a new roomCategory record.")
    @ApiResponse(responseCode = "201", description = "RoomCategory created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>> createRoomCategory(@Valid @RequestBody RoomCategoryDto roomCategoryDto){
        RoomCategoryDto savedRoomCategory = roomCategoryService.createRoomCategory(roomCategoryDto);
        CustomApiResponse<RoomCategoryDto> response = new CustomApiResponse<>(
                "RoomCategory created successfully",
                true,
                savedRoomCategory
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing roomCategory using the provided RoomCategoryDto.
     *
     * This method accepts the roomCategory's ID and a DTO containing updated roomCategory details.
     * It updates the roomCategory record if it exists and returns the updated RoomCategoryDto object.
     *
     * @param id the ID of the roomCategory to be updated
     * @param roomCategoryDto the DTO containing updated roomCategory details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoomCategoryDto,
     *         or a NOT FOUND response if the roomCategory does not exist
     */
    @Operation(summary = "Update roomCategory", description = "Update the details of an existing roomCategory.")
    @ApiResponse(responseCode = "200", description = "RoomCategory updated successfully")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>>  updateRoomCategory(
            @PathVariable Long id,
            @RequestBody RoomCategoryDto roomCategoryDto) {
        Optional<RoomCategoryDto> roomCategoryDtoOptional = roomCategoryService.getRoomCategoryById(id);
        if (roomCategoryDtoOptional.isPresent()) {
            RoomCategoryDto updatedRoomCategory = roomCategoryService.updateRoomCategory(id, roomCategoryDto);
            CustomApiResponse<RoomCategoryDto> response = new CustomApiResponse<>(
                    "RoomCategory updated successfully",
                    true,
                    updatedRoomCategory
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoomCategoryDto> response = new CustomApiResponse<>(
                    "RoomCategory not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }





    /**
     * Delete a roomCategory by their ID.
     *
     * This method deletes the roomCategory record based on the given ID if it exists.
     *
     * @param id the ID of the roomCategory to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the roomCategory does not exist
     */
    @Operation(summary = "Delete RoomCategory", description = "Delete a roomCategory by its ID.")
    @ApiResponse(responseCode = "204", description = "RoomCategory deleted successfully.")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        Optional<RoomCategoryDto> roomCategoryDto = roomCategoryService.getRoomCategoryById(id);
        if (roomCategoryDto.isPresent()) {
            roomCategoryService.deleteRoomCategory(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "RoomCategory deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "RoomCategory not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }

}
