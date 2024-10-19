package revolusion.developers.hms.payload;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolusion.developers.hms.entity.status.RoomStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Room DTO is used for transferring room data across the application.")
public class RoomDto {

    @Schema(description = "Unique ID of the room",
            example = "1")
    private Long id;

    @NotBlank(message = "Room number cannot be blank")
    @Size(max = 10, message = "Room number must be less than or equal to 10 characters")
    @Schema(description = "Room number",
            example = "101",
            required = true)
    private String roomNumber;

    @Schema(description = "Current status of the room",
            example = "BOOKED")
    @JsonIgnore
    private RoomStatus roomStatus;

    @Schema(description = "The category of the room.",
            required = true)
    private RoomCategoryDto roomCategoryDto;

    @Schema(description = "The hotel that the room belongs to.",
            required = true)
    private HotelDto hotelDto;
}
