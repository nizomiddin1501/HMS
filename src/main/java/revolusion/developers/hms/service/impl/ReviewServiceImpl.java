package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Review;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.ReviewException;
import revolusion.developers.hms.payload.ReviewDto;
import revolusion.developers.hms.repository.ReviewRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.ReviewService;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long reviewId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) throws ReviewException {
        return null;
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteReview(Long reviewId) throws ResourceNotFoundException {

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
