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
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.service.PaymentService;

import java.util.Optional;

/**
 * Controller for handling requests related to Payment operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting payment information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Retrieve a paginated list of payments.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of payments per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated PaymentDto list
     */
    @Operation(summary = "Get all Payments with Pagination", description = "Retrieve a paginated list of all payments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of payments.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<PaymentDto>>> getAllPayments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PaymentDto> paymentDtos = paymentService.getAllPayments(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of payments.",
                true,
                paymentDtos), HttpStatus.OK);
    }


    /**
     * Retrieve a payment by their unique ID using the provided PaymentDto.
     *
     * @param id the ID of the payment to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the PaymentDto and
     * an HTTP status of OK
     */
    @Operation(summary = "Get Payment by ID", description = "Retrieve a payment by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment.")
    @ApiResponse(responseCode = "404", description = "Payment not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PaymentDto>> getPaymentById(@PathVariable Long id) {
        PaymentDto paymentDto = paymentService.getPaymentById(id)
                .orElseThrow(() -> new PaymentException("Payment not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the payment.",
                true,
                paymentDto), HttpStatus.OK);
        }


    /**
     * Creates a new payment.
     *
     * @param paymentDto the DTO containing the payment information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved payment data
     */
    @Operation(summary = "Create a new Payment", description = "Create a new payment record.")
    @ApiResponse(responseCode = "201", description = "Payment created successfully.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<PaymentDto>> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        PaymentDto savedPayment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Payment created successfully",
                true,
                savedPayment), HttpStatus.CREATED);
    }


    /**
     * Update the details of an existing payment using the provided PaymentDto.
     *
     * @param id         the ID of the payment to be updated
     * @param paymentDto the DTO containing updated payment details
     * @return a ResponseEntity containing a CustomApiResponse with the updated PaymentDto
     */
    @Operation(summary = "Update payment", description = "Update the details of an existing payment.")
    @ApiResponse(responseCode = "200", description = "Payment updated successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PaymentDto>> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentDto paymentDto) {
        PaymentDto updatedPayment = paymentService.updatePayment(id, paymentDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Payment updated successfully",
                true,
                updatedPayment), HttpStatus.OK);
        }


    /**
     * Delete a payment by their ID.
     *
     * @param id the ID of the payment to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Payment", description = "Delete a payment by its ID.")
    @ApiResponse(responseCode = "204", description = "Payment deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Payment not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Payment deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }
}


