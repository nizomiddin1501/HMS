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
     * Retrieve a paginated list of users.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of users per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated UserDto list
     */
    @Operation(summary = "Get all Users with Pagination", description = "Retrieve a paginated list of all users.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<UserDto> userDtos = userService.getAllUsers(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of users.",
                true,
                userDtos), HttpStatus.OK);
    }


    /**
     * Retrieve a user by their unique ID using the provided UserDto.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the UserDto and
     *         an HTTP status of OK
     */
    @Operation(summary = "Get User by ID", description = "Retrieve a user by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the user.")
    @ApiResponse(responseCode = "404", description = "User not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id)
                .orElseThrow(() -> new UserException("User not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the user.",
                true,
                userDto), HttpStatus.OK);
    }


    /**
     * Creates a new user.
     * <p>
     * This method validates the incoming user data (received via DTO) and sends a verification code
     * to the user's email. User data will not be saved in the database until the verification is completed.
     *
     * @param userDto the DTO containing the user information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the message of successful creation initiation
     */
    @Operation(summary = "Create a new User", description = "Creates a new user and sends a verification code to the email.")
    @ApiResponse(responseCode = "201", description = "User creation process initiated successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<String>> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "User creation process initiated successfully. Please verify your email.",
                true,
                null), HttpStatus.CREATED);
    }


    /**
     * Verifies the user's email using the verification code.
     * <p>
     * After verification, the user's data is saved in the database.
     *
     * @param email            the email of the user
     * @param verificationCode the verification code sent to the user
     * @return a ResponseEntity indicating the result of the verification
     */
    @Operation(summary = "Verify User Email", description = "Verifies the user's email with the provided verification code.")
    @ApiResponse(responseCode = "200", description = "User verified successfully.")
    @PostMapping("/verify")
    public ResponseEntity<CustomApiResponse<String>> verifyUser(
            @RequestParam String email,
            @RequestParam String verificationCode) {
        userService.verifyUser(email, verificationCode);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "User verified successfully. Your account is now active.",
                true,
                null), HttpStatus.OK);
    }


    /**
     * Sends a reset password email to the user.
     * <p>
     * This method generates a reset code and sends an email to the user to initiate the password reset process.
     *
     * @param email the email address of the user who requested the password reset
     * @return a ResponseEntity containing a message indicating the result of the operation
     */
    @Operation(summary = "Send Reset Password Email", description = "Send a reset password email to the user.")
    @ApiResponse(responseCode = "200", description = "Reset password email sent successfully.")
    @PostMapping("/reset-password")
    public ResponseEntity<CustomApiResponse<String>> sendResetPasswordEmail(@RequestParam String email) {
        userService.sendResetPasswordEmail(email);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Reset password email sent successfully",
                true,
                null), HttpStatus.OK);
    }


    /**
     * Resets the user's password.
     * <p>
     * This method verifies the reset code and updates the user's password if the code is valid.
     *
     * @param email       the email address of the user who requested the password reset
     * @param resetCode   the reset code sent to the user
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
        userService.resetPassword(email, resetCode, newPassword);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Password reset successfully",
                true,
                null), HttpStatus.OK);
    }


    /**
     * Update the details of an existing user using the provided UserDto.
     * <p>
     * This method accepts the user's ID and a DTO containing updated user details.
     * It updates the user record if it exists and returns the updated UserDto object.
     *
     * @param id      the ID of the user to be updated
     * @param userDto the DTO containing updated user details
     * @return a ResponseEntity containing a CustomApiResponse with the updated UserDto,
     * or a NOT FOUND response if the user does not exist
     */
    @Operation(summary = "Update user", description = "Update the details of an existing user.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "User updated successfully",
                true,
                updatedUser), HttpStatus.OK);
    }


    /**
     * Delete a user by their ID.
     * <p>
     * This method deletes the user record based on the given ID if it exists.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     * or NOT FOUND if the user does not exist
     */
    @Operation(summary = "Delete User", description = "Delete a user by its ID.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully.")
    @ApiResponse(responseCode = "404", description = "User not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "User deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }
}

