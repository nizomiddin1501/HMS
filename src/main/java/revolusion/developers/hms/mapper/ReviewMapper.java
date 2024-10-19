package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Review;
import revolusion.developers.hms.payload.ReviewDto;

@Mapper(componentModel = "spring",
        uses = { UserMapper.class, OrderMapper.class, HotelMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(source = "user", target = "userDto")
    @Mapping(source = "order", target = "orderDto")
    @Mapping(source = "hotel", target = "hotelDto")
    ReviewDto reviewToDto(Review review);

    @Mapping(source = "userDto", target = "user")
    @Mapping(source = "orderDto", target = "order")
    @Mapping(source = "hotelDto", target = "hotel")
    Review dtoToReview(ReviewDto reviewDto);

}
