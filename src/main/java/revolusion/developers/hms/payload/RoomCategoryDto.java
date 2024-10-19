package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "RoomCategoryDto represents data transfer object for room category.")
public class RoomCategoryDto {

    @Schema(description = "Unique ID of the room category",
            example = "1")
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 50, message = "Category name must be less than or equal to 50 characters")
    @Schema(description = "Name of the room category",
            example = "LUX",
            required = true)
    private String categoryName;

    @NotNull(message = "Price cannot be null")
    @Schema(description = "Price per night for the room category",
            example = "150.00",
            required = true)
    private Double price;
}
