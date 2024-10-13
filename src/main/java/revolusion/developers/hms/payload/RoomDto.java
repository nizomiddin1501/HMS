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

    @NotBlank(message = "Room number cannot be blank")
    @Size(max = 10, message = "Room number must be less than or equal to 10 characters")
    @Schema(description = "Room number",
            example = "101",
            required = true)
    private String roomNumber;

    @NotBlank(message = "Room category cannot be blank")
    @Schema(description = "Category of the room",
            example = "lux",
            required = true)
    private String category;

    @Schema(description = "Price per night for the room",
            example = "150.00")
    private Double price;

    @Schema(description = "Hotel associated with the room")
    private HotelDto hotelDto;









}
