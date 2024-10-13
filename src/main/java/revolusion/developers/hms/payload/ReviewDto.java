package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Review DTO is used for transferring review data across the application.")
public class ReviewDto {

    @Schema(description = "Unique ID of the review", example = "1", hidden = true)
    private Long id;

    @NotBlank(message = "Review content cannot be blank")
    @Schema(description = "Content of the review",
            example = "This hotel offers exceptional services. Highly recommended!",
            required = true)
    private String content;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Schema(description = "Rating provided by the user",
            example = "5",
            required = true)
    private Integer rating;

    @Schema(description = "Date when the review was submitted",
            example = "2024-01-01")
    private Date reviewDate;

    @Schema(description = "User who submitted the review",
            example = "UserDto(id=1, name=Nizomiddin Mirzanazarov, email=nizomiddinmirzanazarov@example.com)")
    private UserDto userDto;

    @Schema(description = "Hotel the review is related to",
            example = "HotelDto(id=1, name=Hotel Palace, category=Luxury, location=Tashkent)")
    private HotelDto hotelDto;







}
