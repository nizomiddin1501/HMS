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
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.service.RoomService;

import java.util.Optional;

/**
 * Controller for handling requests related to Room operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting room information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;


    /**
     * Retrieve a paginated list of all rooms.
     *
     * This method fetches a paginated list of room records and returns them as a list of RoomDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of rooms per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of RoomDto representing all rooms
     */
    @Operation(summary = "Get all Rooms with Pagination", description = "Retrieve a paginated list of all rooms.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of rooms.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoomDto>>> getAllRooms(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<RoomDto> roomDtos = roomService.getAllRooms(page,size);
        CustomApiResponse<Page<RoomDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of rooms.",
                true,
                roomDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Retrieve a room by their unique ID using the provided RoomDto.
     *
     * This method retrieves a room's details based on their ID and returns
     * a CustomApiResponse containing the corresponding RoomDto if found.
     * If the room does not exist, it returns a CustomApiResponse with a
     * message indicating that the room was not found and a 404 Not Found status.
     *
     * @param id the ID of the room to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the RoomDto and
     *         an HTTP status of OK, or a NOT FOUND status if the room does not exist.
     */
    @Operation(summary = "Get Room by ID", description = "Retrieve a room by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the room.")
    @ApiResponse(responseCode = "404", description = "Room not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomDto>> getRoomById(@PathVariable Long id) {
        Optional<RoomDto> roomDto = roomService.getRoomById(id);
        if (roomDto.isPresent()){
            CustomApiResponse<RoomDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the room.",
                    true,
                    roomDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoomDto> response = new CustomApiResponse<>(
                    "User not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new room.
     *
     * This method validates the incoming room data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param roomDto the DTO containing the room information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved room data
     */
    @Operation(summary = "Create a new Room", description = "Create a new room record.")
    @ApiResponse(responseCode = "201", description = "Room created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoomDto>> createRoom(@Valid @RequestBody RoomDto roomDto){
        RoomDto savedRoom = roomService.createRoom(roomDto);
        CustomApiResponse<RoomDto> response = new CustomApiResponse<>(
                "Room created successfully",
                true,
                savedRoom
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing room using the provided RoomDto.
     *
     * This method accepts the room's ID and a DTO containing updated room details.
     * It updates the room record if it exists and returns the updated RoomDto object.
     *
     * @param id the ID of the room to be updated
     * @param roomDto the DTO containing updated room details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoomDto,
     *         or a NOT FOUND response if the room does not exist
     */
    @Operation(summary = "Update room", description = "Update the details of an existing room.")
    @ApiResponse(responseCode = "200", description = "Room updated successfully")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomDto>>  updateRoom(
            @PathVariable Long id,
            @RequestBody RoomDto roomDto) {
        Optional<RoomDto> roomDtoOptional = roomService.getRoomById(id);
        if (roomDtoOptional.isPresent()) {
            RoomDto updatedRoom = roomService.updateRoom(id, roomDto);
            CustomApiResponse<RoomDto> response = new CustomApiResponse<>(
                    "Room updated successfully",
                    true,
                    updatedRoom
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoomDto> response = new CustomApiResponse<>(
                    "Room not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a room by their ID.
     *
     * This method deletes the room record based on the given ID if it exists.
     *
     * @param id the ID of the room to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the room does not exist
     */
    @Operation(summary = "Delete Room", description = "Delete a room by its ID.")
    @ApiResponse(responseCode = "204", description = "Room deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Room not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        Optional<RoomDto> roomDto = roomService.getRoomById(id);
        if (roomDto.isPresent()) {
            roomService.deleteRoom(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Room deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Room not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }


}
