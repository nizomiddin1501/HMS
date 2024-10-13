package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hotel DTO is used for transferring hotel data across the application.")
public class HotelDto {

    @Schema(description = "Unique ID of the hotel", example = "1", hidden = true)
    private Long id;

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(max = 50, message = "Hotel name must be less than or equal to 50 characters")
    @Schema(description = "Name of the hotel",
            example = "Grand Plaza",
            required = true)
    private String name;

    @NotBlank(message = "Location cannot be blank")
    @Schema(description = "Location of the hotel",
            example = "New York, 5th Avenue",
            required = true)
    private String address;

    @Schema(description = "Rating of the hotel",
            example = "4")
    private Integer starRating;

    @Schema(description = "Brief description about the hotel",
            example = "A luxury hotel located in the heart of the city.")
    private String description;







}
