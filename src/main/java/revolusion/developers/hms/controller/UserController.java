package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.service.EmailService;
import revolusion.developers.hms.service.UserService;

import java.util.Optional;

/**
 * Controller for handling requests related to User operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting user information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final EmailService emailService;



    /**
     * Retrieve a paginated list of all users.
     *
     * This method fetches a paginated list of user records and returns them as a list of UserDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of users per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of UserDto representing all users
     */
    @Operation(summary = "Get all Users with Pagination", description = "Retrieve a paginated list of all users.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<UserDto> userDtos = userService.getAllUsers(page,size);
        CustomApiResponse<Page<UserDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of users.",
                true,
                userDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Retrieve a user by their unique ID using the provided UserDto.
     *
     * This method retrieves a user's details based on their ID and returns
     * a CustomApiResponse containing the corresponding UserDto if found.
     * If the user does not exist, it returns a CustomApiResponse with a
     * message indicating that the user was not found and a 404 Not Found status.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the UserDto and
     *         an HTTP status of OK, or a NOT FOUND status if the user does not exist.
     */
    @Operation(summary = "Get User by ID", description = "Retrieve a user by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the user.")
    @ApiResponse(responseCode = "404", description = "User not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        if (userDto.isPresent()){
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the user.",
                    true,
                    userDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    "User not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new user.
     *
     * This method validates the incoming user data (received via DTO) and saves it to the database
     * if valid. After creating the user, an email is sent to notify the user of the successful registration.
     *
     * @param userDto the DTO containing the user information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved user data
     */
    @Operation(summary = "Create a new User", description = "Create a new user record.")
    @ApiResponse(responseCode = "201", description = "User created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            UserDto savedUser = userService.createUser(userDto);
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    "User created successfully",
                    true,
                    savedUser
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserException e) {
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * Verify the user's email using the verification code.
     *
     * @param email the email of the user
     * @param verificationCode the verification code sent to the user
     * @return a ResponseEntity indicating the result of the verification
     */
    @PostMapping("/verify")
    public ResponseEntity<CustomApiResponse<String>> verifyUser(
            @RequestParam String email,
            @RequestParam String verificationCode) {
        try {
            userService.verifyUser(email, verificationCode);
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    "User verified successfully",
                    true,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserException e) {
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * Sends a reset password email to the user.
     *
     * This method generates a reset code and sends an email to the user to initiate the password reset process.
     *
     * @param email the email address of the user who requested the password reset
     * @return a ResponseEntity containing a message indicating the result of the operation
     */
    @Operation(summary = "Send Reset Password Email", description = "Send a reset password email to the user.")
    @ApiResponse(responseCode = "200", description = "Reset password email sent successfully.")
    @PostMapping("/reset-password")
    public ResponseEntity<CustomApiResponse<String>> sendResetPasswordEmail(@RequestParam String email) {
        try {
            userService.sendResetPasswordEmail(email);
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    "Reset password email sent successfully",
                    true,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserException e) {
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }




    /**
     * Resets the user's password.
     *
     * This method verifies the reset code and updates the user's password if the code is valid.
     *
     * @param email the email address of the user who requested the password reset
     * @param resetCode the reset code sent to the user
     * @param newPassword the new password to set for the user
     * @return a ResponseEntity containing a message indicating the result of the operation
     */
    @Operation(summary = "Reset Password", description = "Reset the user's password using the reset code.")
    @ApiResponse(responseCode = "200", description = "Password reset successfully.")
    @PostMapping("/reset-password-confirm")
    public ResponseEntity<CustomApiResponse<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String resetCode,
            @RequestParam String newPassword) {
        try {
            userService.resetPassword(email, resetCode, newPassword);
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    "Password reset successfully",
                    true,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserException e) {
            CustomApiResponse<String> response = new CustomApiResponse<>(
                    e.getMessage(),
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }






    /**
     * Update the details of an existing user using the provided UserDto.
     *
     * This method accepts the user's ID and a DTO containing updated user details.
     * It updates the user record if it exists and returns the updated UserDto object.
     *
     * @param id the ID of the user to be updated
     * @param userDto the DTO containing updated user details
     * @return a ResponseEntity containing a CustomApiResponse with the updated UserDto,
     *         or a NOT FOUND response if the user does not exist
     */
    @Operation(summary = "Update user", description = "Update the details of an existing user.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserDto>>  updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        Optional<UserDto> userDtoOptional = userService.getUserById(id);
        if (userDtoOptional.isPresent()) {
            UserDto updatedUser = userService.updateUser(id, userDto);
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    "User updated successfully",
                    true,
                    updatedUser
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<UserDto> response = new CustomApiResponse<>(
                    "User not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }





    /**
     * Delete a user by their ID.
     *
     * This method deletes the user record based on the given ID if it exists.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the user does not exist
     */
    @Operation(summary = "Delete User", description = "Delete a user by its ID.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully.")
    @ApiResponse(responseCode = "404", description = "User not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteUser(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        if (userDto.isPresent()) {
            userService.deleteUser(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "User deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "User not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }






}
