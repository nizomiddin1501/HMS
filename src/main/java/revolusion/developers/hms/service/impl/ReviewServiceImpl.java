package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Review;
import revolusion.developers.hms.entity.Role;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.payload.ReviewDto;
import revolusion.developers.hms.repository.ReviewRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ModelMapper modelMapper;

    private final ReviewRepository reviewRepository;


    @Autowired
    public ReviewServiceImpl(ModelMapper modelMapper, ReviewRepository reviewRepository) {
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
    }


    @Override
    public List<ReviewDto> getAllReviews(int page, int size) {
        List<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size)).getContent();
        return reviews.stream()
                .map(this::reviewToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long reviewId) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));

        // Convert Review entity to ReviewDto
        ReviewDto reviewDto = reviewToDto(review);
        return Optional.ofNullable(reviewDto);
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) throws ReviewException {
        // 1. Convert DTO to entity
        Review review = dtoToReview(reviewDto);

        // 2. Perform business checks on the entity( User, Order, Hotel with)
        if (reviewDto.getUserDto() == null || reviewDto.getUserDto().getId() == null) {
            throw new ReviewException("User is required for the review.");
        }

        if (reviewDto.getOrderDto() == null || reviewDto.getOrderDto().getId() == null) {
            throw new ReviewException("Order is required for the review.");
        }

        if (reviewDto.getHotelDto() == null || reviewDto.getHotelDto().getId() == null) {
            throw new ReviewException("Hotel is required for the review.");
        }

        // 3. Save Review
        Review savedReview = reviewRepository.save(review);

        // 4. Convert the saved User to DTO and return
        return reviewToDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) throws ResourceNotFoundException {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));

        // update review details
        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());

        // Save updated review
        Review updatedReview = reviewRepository.save(existingReview);

        // Convert updated review entity to DTO and return
        return reviewToDto(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));
        reviewRepository.delete(review);
    }

    // DTO ---> Entity
    private Review dtoToReview(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, Review.class);
    }

    // Entity ---> DTO
    public ReviewDto reviewToDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

}
