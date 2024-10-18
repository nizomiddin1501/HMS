package revolusion.developers.hms.service.impl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.*;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.payload.ReviewDto;
import revolusion.developers.hms.repository.*;
import revolusion.developers.hms.service.ReviewService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final HotelRepository hotelRepository;




    @Override
    public Page<ReviewDto> getAllReviews(int page, int size) {
        Page<Review> reviewsPage = reviewRepository.findAll(PageRequest.of(page, size));
        return reviewsPage.map(this::reviewToDto);
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long reviewId) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));
        ReviewDto reviewDto = reviewToDto(review);
        return Optional.ofNullable(reviewDto);
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) throws ReviewException {
        Review review = dtoToReview(reviewDto);
        if (reviewDto.getUserDto() == null || reviewDto.getUserDto().getId() == null) {
            throw new ReviewException("User is required for the review.");
        }
        if (reviewDto.getOrderDto() == null || reviewDto.getOrderDto().getId() == null) {
            throw new ReviewException("Order is required for the review.");
        }
        if (reviewDto.getHotelDto() == null || reviewDto.getHotelDto().getId() == null) {
            throw new ReviewException("Hotel is required for the review.");
        }
        User user = userRepository.findById(reviewDto.getUserDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", reviewDto.getUserDto().getId()));

        Order order = orderRepository.findById(reviewDto.getOrderDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Id", reviewDto.getOrderDto().getId()));

        Hotel hotel = hotelRepository.findById(reviewDto.getHotelDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "Id", reviewDto.getHotelDto().getId()));

        review.setUser(user);
        review.setOrder(order);
        review.setHotel(hotel);

        Review savedReview = reviewRepository.save(review);
        return reviewToDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) throws ResourceNotFoundException {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));
        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        Review updatedReview = reviewRepository.save(existingReview);
        return reviewToDto(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", " Id ", reviewId));
        reviewRepository.delete(review);
    }


    private Review dtoToReview(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, Review.class);
    }
    public ReviewDto reviewToDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

}
