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
import revolusion.developers.hms.exceptions.RoomException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.RoomDto;
import revolusion.developers.hms.service.RoomService;

/**
 * REST controller for managing rooms, offering endpoints for
 * creating, updating, retrieving, and deleting room records.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    /**
     * Retrieve a paginated list of rooms.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of rooms per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated RoomDto list
     */
    @Operation(summary = "Get all Rooms with Pagination", description = "Retrieve a paginated list of all rooms.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of rooms.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoomDto>>> getAllRooms(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<RoomDto> roomDtos = roomService.getAllRooms(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of rooms.",
                true,
                roomDtos), HttpStatus.OK);
    }


    /**
     * Retrieve a room by their unique ID using the provided RoomDto.
     *
     * @param id the ID of the room to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the RoomDto and
     * an HTTP status of OK
     */
    @Operation(summary = "Get Room by ID", description = "Retrieve a room by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the room.")
    @ApiResponse(responseCode = "404", description = "Room not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomDto>> getRoomById(@PathVariable Long id) {
        RoomDto roomDto = roomService.getRoomById(id)
                .orElseThrow(() -> new RoomException("Room not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the room.",
                true,
                roomDto), HttpStatus.OK);
    }


    /**
     * Creates a new room.
     *
     * @param roomDto the DTO containing the room information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved room data
     */
    @Operation(summary = "Create a new Room", description = "Create a new room record.")
    @ApiResponse(responseCode = "201", description = "Room created successfully.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoomDto>> createRoom(@Valid @RequestBody RoomDto roomDto) {
        RoomDto savedRoom = roomService.createRoom(roomDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Room created successfully",
                true,
                savedRoom), HttpStatus.CREATED);
    }


    /**
     * Update the details of an existing room using the provided RoomDto.
     *
     * @param id      the ID of the room to be updated
     * @param roomDto the DTO containing updated room details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoomDto
     */
    @Operation(summary = "Update room", description = "Update the details of an existing room.")
    @ApiResponse(responseCode = "200", description = "Room updated successfully")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoomDto>> updateRoom(
            @PathVariable Long id,
            @RequestBody RoomDto roomDto) {
        RoomDto updatedRoom = roomService.updateRoom(id, roomDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Room updated successfully",
                true,
                updatedRoom), HttpStatus.OK);
    }


    /**
     * Delete a room by their ID.
     *
     * @param id the ID of the room to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Room", description = "Delete a room by its ID.")
    @ApiResponse(responseCode = "204", description = "Room deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Room not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Room deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }
}
