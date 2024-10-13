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
@Schema(description = "Room DTO is used for transferring room data across the application.")
public class RoomDto {

    @Schema(description = "Unique ID of the room",
            example = "1",
            hidden = true)
    private Long id;

    @NotBlank(message = "Room type cannot be blank")
    @Size(max = 30, message = "Room type must be less than or equal to 30 characters")
    @Schema(description = "Type of the room",
            example = "Luxury Suite",
            required = true)
    private String type;

    @Schema(description = "Price per night for the room",
            example = "150")
    private Double price;

    @Schema(description = "Description of the room",
            example = "A luxurious suite with sea view.")
    private String description;

    @Schema(description = "Hotel associated with the room")
    private HotelDto hotelDto;









}
