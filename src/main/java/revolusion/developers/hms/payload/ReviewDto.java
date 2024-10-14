package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Review DTO is used for transferring review data across the application.")
public class ReviewDto {

    @Schema(description = "Unique ID of the review", example = "1", hidden = true)
    private Long id;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Schema(description = "Rating provided by the user",
            example = "5",
            required = true)
    private Integer rating;


    @Schema(description = "Comment or review text given by the user",
            example = "Great room, had a fantastic stay!")
    private String comment;

    @Schema(description = "User who submitted the review",
            example = "UserDto(id=1, name=Nizomiddin Mirzanazarov, email=nizomiddinmirzanazarov@example.com)")
    private UserDto userDto;

    @Schema(description = "Order associated with the review",
            example = "OrderDto(id=1, totalAmount=450.00)")
    private OrderDto orderDto;

    @Schema(description = "Hotel associated with the review",
            example = "HotelDto(id=1, name=Hotel Palace, category=Luxury, location=Tashkent)")
    private HotelDto hotelDto;

}
