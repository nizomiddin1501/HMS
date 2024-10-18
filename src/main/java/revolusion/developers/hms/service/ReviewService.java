package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.payload.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {


    Page<ReviewDto> getAllReviews(int page, int size);

    Optional<ReviewDto> getReviewById(Long reviewId) throws ResourceNotFoundException;

    ReviewDto createReview(ReviewDto reviewDto) throws ReviewException;

    ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) throws ResourceNotFoundException;

    void deleteReview(Long reviewId) throws ResourceNotFoundException;









}
