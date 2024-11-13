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
import revolusion.developers.hms.exceptions.UserPaymentException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.UserPaymentDto;
import revolusion.developers.hms.service.UserPaymentService;

/**
 * REST controller for managing userPayments, offering endpoints for
 * creating, updating, retrieving, and deleting userPayment records.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userPayments")
public class UserPaymentController {

    private final UserPaymentService userPaymentService;

    /**
     * Retrieve a paginated list of userPayments.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of userPayments per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated UserPaymentDto list
     */
    @Operation(summary = "Get all UserPayments with Pagination", description = "Retrieve a paginated list of all userPayments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of userPayments.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<UserPaymentDto>>> getAllUserPayments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<UserPaymentDto> userPayments = userPaymentService.getAllUserPayments(page, size);
        return ResponseEntity.ok(new CustomApiResponse<>(
                "Successfully retrieved the list of users.",
                true,
                userPayments));
    }



    /**
     * Retrieve a userPayment by their unique ID using the provided UserPaymentDto.
     *
     * @param id the ID of the userPayment to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the UserPaymentDto and
     *         an HTTP status of OK
     */
    @Operation(summary = "Get UserPayment by ID", description = "Retrieve a userPayment by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the userPayment.")
    @ApiResponse(responseCode = "404", description = "UserPayment not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserPaymentDto>> getUserPaymentById(@PathVariable Long id) {
        UserPaymentDto userPaymentDto = userPaymentService.getUserPaymentById(id)
                .orElseThrow(() -> new UserPaymentException("UserPayment not found with id: " + id));

        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the userPayment.",
                true,
                userPaymentDto), HttpStatus.OK);
    }



    /**
     * Creates a new userPayment.
     *
     * @param userPaymentDto the DTO containing the userPayment information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved userPayment data
     */
    @Operation(summary = "Create a new UserPayment", description = "Create a new userPayment record.")
    @ApiResponse(responseCode = "201", description = "UserPayment created successfully.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<UserPaymentDto>> createUserPayment(@Valid @RequestBody UserPaymentDto userPaymentDto){
        UserPaymentDto savedUserPayment = userPaymentService.createUserPayment(userPaymentDto);
        CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                "UserPayment created successfully",
                true,
                savedUserPayment
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing userPayment using the provided UserPaymentDto.
     *
     * @param id the ID of the userPayment to be updated
     * @param userPaymentDto the DTO containing updated userPayment details
     * @return a ResponseEntity containing a CustomApiResponse with the updated UserPaymentDto
     */
    @Operation(summary = "Update userPayment", description = "Update the details of an existing userPayment.")
    @ApiResponse(responseCode = "200", description = "UserPayment updated successfully")
    @ApiResponse(responseCode = "404", description = "UserPayment not found")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserPaymentDto>> updateUserPayment(
            @PathVariable Long id,
            @RequestBody UserPaymentDto userPaymentDto) {
        UserPaymentDto updatedUserPayment = userPaymentService.updateUserPayment(id, userPaymentDto);
        CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                "UserPayment updated successfully",
                true,
                updatedUserPayment
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Delete a userPayment by their ID.
     *
     * @param id the ID of the userPayment to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete UserPayment", description = "Delete a userPayment by its ID.")
    @ApiResponse(responseCode = "204", description = "UserPayment deleted successfully.")
    @ApiResponse(responseCode = "404", description = "UserPayment not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteUserPayment(@PathVariable Long id) {
        userPaymentService.deleteUserPayment(id);
        CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                "UserPayment deleted successfully.",
                true,
                null);
        return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
    }




}
