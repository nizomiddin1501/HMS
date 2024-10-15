package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.payload.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {


    // get all reviews using pagination
    Page<ReviewDto> getAllReviews(int page, int size);

    // get review by ID
    Optional<ReviewDto> getReviewById(Long reviewId) throws ResourceNotFoundException;

    // create a new review
    ReviewDto createReview(ReviewDto reviewDto) throws ReviewException;

    // update an existing review
    ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) throws ResourceNotFoundException;

    // delete a review
    void deleteReview(Long reviewId) throws ResourceNotFoundException;









}
