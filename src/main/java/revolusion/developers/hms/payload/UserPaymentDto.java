package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserPayment DTO is used for transferring payment data associated with the user across the application.")
public class UserPaymentDto {



    @Schema(description = "Unique ID of the payment",
            example = "1",
            hidden = true)
    private Long id;

    @NotNull(message = "Balance cannot be null")
    @Positive(message = "Balance must be greater than zero")
    @Schema(description = "User's account balance",
            example = "1500.0",
            required = true)
    private Double balance;

    @NotBlank(message = "Account number cannot be blank")
    @Schema(description = "User's account number",
            example = "123456789",
            required = true)
    private String accountNumber;

    @NotNull(message = "User cannot be null")
    @Schema(description = "Reference to the user associated with the payment.")
    private UserDto userDto;

}
