package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.exceptions.RoomCategoryException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.RoomCategoryDto;
import revolusion.developers.hms.payload.UserDto;
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
     * Retrieve a paginated list of roomCategories.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of roomCategories per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated RoomCategoryDto list
     */
    @Operation(summary = "Get all RoomCategories with Pagination", description = "Retrieve a paginated list of all roomCategories.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of roomCategories.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoomCategoryDto>>> getAllRoomCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<RoomCategoryDto> roomCategoryDtos = roomCategoryService.getAllRoomCategories(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of roomCategories.",
                true,
                roomCategoryDtos), HttpStatus.OK);
    }


    /**
     * Retrieve a roomCategory by their unique ID using the provided RoomCategoryDto.
     *
     * @param id the ID of the roomCategory to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the RoomCategoryDto and
     * an HTTP status of OK
     */
    @Operation(summary = "Get RoomCategory by ID", description = "Retrieve a roomCategory by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the roomCategory.")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>> getRoomCategoryById(@PathVariable Long id) {
        RoomCategoryDto roomCategoryDto = roomCategoryService.getRoomCategoryById(id)
                .orElseThrow(() -> new RoomCategoryException("RoomCategory not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the roomCategory.",
                true,
                roomCategoryDto), HttpStatus.OK);
        }


    /**
     * Creates a new roomCategory.
     *
     * @param roomCategoryDto the DTO containing the roomCategory information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved roomCategory data
     */
    @Operation(summary = "Create a new RoomCategory", description = "Create a new roomCategory record.")
    @ApiResponse(responseCode = "201", description = "RoomCategory created successfully.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>> createRoomCategory(@Valid @RequestBody RoomCategoryDto roomCategoryDto) {
        RoomCategoryDto savedRoomCategory = roomCategoryService.createRoomCategory(roomCategoryDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "RoomCategory created successfully",
                true,
                savedRoomCategory), HttpStatus.CREATED);
    }


    /**
     * Update the details of an existing roomCategory using the provided RoomCategoryDto.
     *
     * @param id              the ID of the roomCategory to be updated
     * @param roomCategoryDto the DTO containing updated roomCategory details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoomCategoryDto
     */
    @Operation(summary = "Update roomCategory", description = "Update the details of an existing roomCategory.")
    @ApiResponse(responseCode = "200", description = "RoomCategory updated successfully")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomCategoryDto>> updateRoomCategory(
            @PathVariable Long id,
            @RequestBody RoomCategoryDto roomCategoryDto) {
        RoomCategoryDto updatedRoomCategory = roomCategoryService.updateRoomCategory(id, roomCategoryDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "RoomCategory updated successfully",
                true,
                updatedRoomCategory), HttpStatus.OK);
    }


    /**
     * Delete a roomCategory by their ID.
     *
     * @param id the ID of the roomCategory to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete RoomCategory", description = "Delete a roomCategory by its ID.")
    @ApiResponse(responseCode = "204", description = "RoomCategory deleted successfully.")
    @ApiResponse(responseCode = "404", description = "RoomCategory not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomCategoryService.deleteRoomCategory(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "RoomCategory deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }

}
