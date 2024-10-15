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

    @Schema(description = "Unique ID of the hotel",
            example = "1")
    private Long id;

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(max = 50, message = "Hotel name must be less than or equal to 50 characters")
    @Schema(description = "Name of the hotel",
            example = "Hyatt",
            required = true)
    private String name;

    @NotBlank(message = "Location cannot be blank")
    @Schema(description = "Location of the hotel",
            example = "Tashkent, Yunusabad 17",
            required = true)
    private String address;

    @Schema(description = "Rating of the hotel",
            example = "4")
    private Integer starRating;

    @Schema(description = "Brief description about the hotel",
            example = "A luxury hotel located in the heart of the city.")
    private String description;

    @Schema(description = "The current balance of the hotel account.",
            example = "10000.00",
            required = true)
    private Double balance;

    @NotBlank(message = "Account number cannot be blank")
    @Size(max = 20, message = "Account number must be less than or equal to 20 characters")
    @Schema(description = "The account number of the hotel.",
            example = "ACCT123456",
            required = true)
    private String accountNumber;







}
