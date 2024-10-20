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
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.ReviewDto;
import revolusion.developers.hms.payload.RoleDto;
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
     * Retrieve a paginated list of reviews.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of reviews per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated ReviewDto list
     */
    @Operation(summary = "Get all Reviews with Pagination", description = "Retrieve a paginated list of all reviews.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of reviews.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<ReviewDto>>> getAllReviews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<ReviewDto> reviewDtos = reviewService.getAllReviews(page,size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of reviews.",
                true,
                reviewDtos), HttpStatus.OK);
    }



    /**
     * Retrieve a review by their unique ID using the provided ReviewDto.
     *
     * @param id the ID of the review to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the ReviewDto and
     *         an HTTP status of OK
     */
    @Operation(summary = "Get Review by ID", description = "Retrieve a review by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the review.")
    @ApiResponse(responseCode = "404", description = "Review not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ReviewDto>> getReviewById(@PathVariable Long id) {
        ReviewDto reviewDto = reviewService.getReviewById(id)
                .orElseThrow(() -> new ReviewException("Review not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the review.",
                true,
                reviewDto), HttpStatus.OK);
        }



    /**
     * Creates a new review.
     *
     * @param reviewDto the DTO containing the review information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved review data
     */
    @Operation(summary = "Create a new Review", description = "Create a new review record.")
    @ApiResponse(responseCode = "201", description = "Review created successfully.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<ReviewDto>> createReview(@Valid @RequestBody ReviewDto reviewDto){
        ReviewDto savedReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Review created successfully",
                true,
                savedReview), HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing review using the provided ReviewDto.
     *
     * @param id the ID of the review to be updated
     * @param reviewDto the DTO containing updated review details
     * @return a ResponseEntity containing a CustomApiResponse with the updated ReviewDto
     */
    @Operation(summary = "Update review", description = "Update the details of an existing review.")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ReviewDto>>  updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(id, reviewDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Review updated successfully",
                true,
                updatedReview), HttpStatus.OK);
        }




    /**
     * Delete a review by their ID.
     *
     * @param id the ID of the review to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Review", description = "Delete a review by its ID.")
    @ApiResponse(responseCode = "204", description = "Review deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Review not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Review deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
        }
    }

