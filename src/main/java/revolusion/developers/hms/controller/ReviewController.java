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
import revolusion.developers.hms.payload.ReviewDto;
import revolusion.developers.hms.service.ReviewService;

import java.util.Optional;

/**
 * Controller for handling requests related to Review operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting review information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {


    private final ReviewService reviewService;



    /**
     * Retrieve a paginated list of all reviews.
     *
     * This method fetches a paginated list of review records and returns them as a list of ReviewDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of reviews per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of ReviewDto representing all reviews
     */
    @Operation(summary = "Get all Reviews with Pagination", description = "Retrieve a paginated list of all reviews.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of reviews.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<ReviewDto>>> getAllReviews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<ReviewDto> reviewDtos = reviewService.getAllReviews(page,size);
        CustomApiResponse<Page<ReviewDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of reviews.",
                true,
                reviewDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * Retrieve a review by their unique ID using the provided ReviewDto.
     *
     * This method retrieves a review's details based on their ID and returns
     * a CustomApiResponse containing the corresponding ReviewDto if found.
     * If the review does not exist, it returns a CustomApiResponse with a
     * message indicating that the review was not found and a 404 Not Found status.
     *
     * @param id the ID of the review to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the ReviewDto and
     *         an HTTP status of OK, or a NOT FOUND status if the review does not exist.
     */
    @Operation(summary = "Get Review by ID", description = "Retrieve a review by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the review.")
    @ApiResponse(responseCode = "404", description = "Review not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ReviewDto>> getReviewById(@PathVariable Long id) {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if (reviewDto.isPresent()){
            CustomApiResponse<ReviewDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the review.",
                    true,
                    reviewDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<ReviewDto> response = new CustomApiResponse<>(
                    "Review not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new review.
     *
     * This method validates the incoming review data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param reviewDto the DTO containing the review information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved review data
     */
    @Operation(summary = "Create a new Review", description = "Create a new review record.")
    @ApiResponse(responseCode = "201", description = "Review created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<ReviewDto>> createReview(@Valid @RequestBody ReviewDto reviewDto){
        ReviewDto savedReview = reviewService.createReview(reviewDto);
        CustomApiResponse<ReviewDto> response = new CustomApiResponse<>(
                "Review created successfully",
                true,
                savedReview
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing role using the provided ReviewDto.
     *
     * This method accepts the review's ID and a DTO containing updated review details.
     * It updates the review record if it exists and returns the updated ReviewDto object.
     *
     * @param id the ID of the review to be updated
     * @param reviewDto the DTO containing updated review details
     * @return a ResponseEntity containing a CustomApiResponse with the updated ReviewDto,
     *         or a NOT FOUND response if the review does not exist
     */
    @Operation(summary = "Update review", description = "Update the details of an existing review.")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ReviewDto>>  updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDto reviewDto) {
        Optional<ReviewDto> reviewDtoOptional = reviewService.getReviewById(id);
        if (reviewDtoOptional.isPresent()) {
            ReviewDto updatedReview = reviewService.updateReview(id, reviewDto);
            CustomApiResponse<ReviewDto> response = new CustomApiResponse<>(
                    "Review updated successfully",
                    true,
                    updatedReview
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<ReviewDto> response = new CustomApiResponse<>(
                    "Review not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }




    /**
     * Delete a review by their ID.
     *
     * This method deletes the review record based on the given ID if it exists.
     *
     * @param id the ID of the review to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the review does not exist
     */
    @Operation(summary = "Delete Review", description = "Delete a review by its ID.")
    @ApiResponse(responseCode = "204", description = "Review deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Review not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteReview(@PathVariable Long id) {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if (reviewDto.isPresent()) {
            reviewService.deleteReview(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Review deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Review not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }


}
