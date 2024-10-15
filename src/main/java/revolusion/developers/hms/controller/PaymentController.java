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
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.service.PaymentService;

import java.util.Optional;

/**
 * Controller for handling requests related to Payment operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting payment information.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Constructor for PaymentController.
     *
     * @param paymentService the service to manage payment records
     * @Autowired automatically injects the PaymentService bean
     */
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }



    /**
     * Retrieve a paginated list of all payments.
     *
     * This method fetches a paginated list of payment records and returns them as a list of PaymentDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of payments per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of PaymentDto representing all payments
     */
    @Operation(summary = "Get all Payments with Pagination", description = "Retrieve a paginated list of all payments.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of payments.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<PaymentDto>>> getAllPayments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<PaymentDto> paymentDtos = paymentService.getAllPayments(page,size);
        CustomApiResponse<Page<PaymentDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of payments.",
                true,
                paymentDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Retrieve a payment by their unique ID using the provided PaymentDto.
     *
     * This method retrieves a payment's details based on their ID and returns
     * a CustomApiResponse containing the corresponding PaymentDto if found.
     * If the payment does not exist, it returns a CustomApiResponse with a
     * message indicating that the payment was not found and a 404 Not Found status.
     *
     * @param id the ID of the payment to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the PaymentDto and
     *         an HTTP status of OK, or a NOT FOUND status if the payment does not exist.
     */
    @Operation(summary = "Get Payment by ID", description = "Retrieve a payment by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the payment.")
    @ApiResponse(responseCode = "404", description = "Payment not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PaymentDto>> getPaymentById(@PathVariable Long id) {
        Optional<PaymentDto> paymentDto = paymentService.getPaymentById(id);
        if (paymentDto.isPresent()){
            CustomApiResponse<PaymentDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the payment.",
                    true,
                    paymentDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<PaymentDto> response = new CustomApiResponse<>(
                    "Payment not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new payment.
     *
     * This method validates the incoming payment data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param paymentDto the DTO containing the payment information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved payment data
     */
    @Operation(summary = "Create a new Payment", description = "Create a new payment record.")
    @ApiResponse(responseCode = "201", description = "Payment created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<PaymentDto>> createPayment(@Valid @RequestBody PaymentDto paymentDto){
        PaymentDto savedPayment = paymentService.createPayment(paymentDto);
        CustomApiResponse<PaymentDto> response = new CustomApiResponse<>(
                "Payment created successfully",
                true,
                savedPayment
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing payment using the provided PaymentDto.
     *
     * This method accepts the payment's ID and a DTO containing updated payment details.
     * It updates the payment record if it exists and returns the updated PaymentDto object.
     *
     * @param id the ID of the payment to be updated
     * @param paymentDto the DTO containing updated payment details
     * @return a ResponseEntity containing a CustomApiResponse with the updated PaymentDto,
     *         or a NOT FOUND response if the payment does not exist
     */
    @Operation(summary = "Update payment", description = "Update the details of an existing payment.")
    @ApiResponse(responseCode = "200", description = "Payment updated successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<PaymentDto>>  updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentDto paymentDto) {
        Optional<PaymentDto> roleDtoOptional = paymentService.getPaymentById(id);
        if (roleDtoOptional.isPresent()) {
            PaymentDto updatedPayment = paymentService.updatePayment(id, paymentDto);
            CustomApiResponse<PaymentDto> response = new CustomApiResponse<>(
                    "Payment updated successfully",
                    true,
                    updatedPayment
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<PaymentDto> response = new CustomApiResponse<>(
                    "Payment not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Delete a payment by their ID.
     *
     * This method deletes the payment record based on the given ID if it exists.
     *
     * @param id the ID of the payment to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the payment does not exist
     */
    @Operation(summary = "Delete Payment", description = "Delete a payment by its ID.")
    @ApiResponse(responseCode = "204", description = "Payment deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Payment not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deletePayment(@PathVariable Long id) {
        Optional<PaymentDto> paymentDto = paymentService.getPaymentById(id);
        if (paymentDto.isPresent()) {
            paymentService.deletePayment(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Payment deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Payment not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }




}
