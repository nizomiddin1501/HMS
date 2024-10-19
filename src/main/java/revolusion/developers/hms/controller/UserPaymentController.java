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
import revolusion.developers.hms.payload.UserPaymentDto;
import revolusion.developers.hms.service.UserPaymentService;

import java.util.Optional;

/**
 * Controller for handling requests related to UserPayment operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting userPayment information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userPayments")
public class UserPaymentController {



    private final UserPaymentService userPaymentService;



    /**
     * Retrieve a paginated list of all userPayments.
     *
     * This method fetches a paginated list of userPayment records and returns them as a list of UserPaymentDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of userPayments per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of UserPaymentDto representing all userPayments
     */
    @Operation(summary = "Get all UserPayments with Pagination", description = "Retrieve a paginated list of all userPayments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of userPayments.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<UserPaymentDto>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<UserPaymentDto> userPaymentDtos = userPaymentService.getAllUserPayments(page,size);
        CustomApiResponse<Page<UserPaymentDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of users.",
                true,
                userPaymentDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Retrieve a userPayment by their unique ID using the provided UserPaymentDto.
     *
     * This method retrieves a userPayment's details based on their ID and returns
     * a CustomApiResponse containing the corresponding UserPaymentDto if found.
     * If the userPayment does not exist, it returns a CustomApiResponse with a
     * message indicating that the userPayment was not found and a 404 Not Found status.
     *
     * @param id the ID of the userPayment to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the UserPaymentDto and
     *         an HTTP status of OK, or a NOT FOUND status if the userPayment does not exist.
     */
    @Operation(summary = "Get UserPayment by ID", description = "Retrieve a userPayment by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the userPayment.")
    @ApiResponse(responseCode = "404", description = "UserPayment not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserPaymentDto>> getUserPaymentById(@PathVariable Long id) {
        Optional<UserPaymentDto> userPaymentDto = userPaymentService.getUserPaymentById(id);
        if (userPaymentDto.isPresent()){
            CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the userPayment.",
                    true,
                    userPaymentDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                    "UserPayment not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new userPayment.
     *
     * This method validates the incoming userPayment data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param userPaymentDto the DTO containing the userPayment information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved userPayment data
     */
    @Operation(summary = "Create a new UserPayment", description = "Create a new userPayment record.")
    @ApiResponse(responseCode = "201", description = "UserPayment created successfully.")
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
     * This method accepts the userPayment's ID and a DTO containing updated userPayment details.
     * It updates the userPayment record if it exists and returns the updated UserPaymentDto object.
     *
     * @param id the ID of the userPayment to be updated
     * @param userPaymentDto the DTO containing updated userPayment details
     * @return a ResponseEntity containing a CustomApiResponse with the updated UserPaymentDto,
     *         or a NOT FOUND response if the userPayment does not exist
     */
    @Operation(summary = "Update userPayment", description = "Update the details of an existing userPayment.")
    @ApiResponse(responseCode = "200", description = "UserPayment updated successfully")
    @ApiResponse(responseCode = "404", description = "UserPayment not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserPaymentDto>>  updateUserPayment(
            @PathVariable Long id,
            @RequestBody UserPaymentDto userPaymentDto) {
        Optional<UserPaymentDto> userPaymentDtoOptional = userPaymentService.getUserPaymentById(id);
        if (userPaymentDtoOptional.isPresent()) {
            UserPaymentDto updatedUserPayment = userPaymentService.updateUserPayment(id, userPaymentDto);
            CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                    "UserPayment updated successfully",
                    true,
                    updatedUserPayment
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<UserPaymentDto> response = new CustomApiResponse<>(
                    "UserPayment not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Delete a userPayment by their ID.
     *
     * This method deletes the userPayment record based on the given ID if it exists.
     *
     * @param id the ID of the userPayment to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the userPayment does not exist
     */
    @Operation(summary = "Delete UserPayment", description = "Delete a userPayment by its ID.")
    @ApiResponse(responseCode = "204", description = "UserPayment deleted successfully.")
    @ApiResponse(responseCode = "404", description = "UserPayment not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteUserPayment(@PathVariable Long id) {
        Optional<UserPaymentDto> userPaymentDto = userPaymentService.getUserPaymentById(id);
        if (userPaymentDto.isPresent()) {
            userPaymentService.deleteUserPayment(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "UserPayment deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "UserPayment not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }




}
