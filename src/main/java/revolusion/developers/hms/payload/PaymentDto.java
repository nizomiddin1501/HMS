package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payment DTO is used for transferring payment data across the application.")
public class PaymentDto {

    @Schema(description = "Unique ID of the payment", example = "1", hidden = true)
    private Long id;

    @Schema(description = "Date of the payment",
            example = "2024-01-01")
    private Date paymentDate;

    @Schema(description = "Total amount paid",
            example = "450.00")
    private Double amount;

    @NotBlank(message = "Payment method cannot be blank")
    @Schema(description = "Payment method used",
            example = "Credit Card")
    private String method;

    @Schema(description = "Order associated with the payment")
    private OrderDto orderDto;













}
