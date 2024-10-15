package revolusion.developers.hms.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolusion.developers.hms.entity.status.PaymentStatus;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payment DTO is used for transferring payment data across the application.")
public class PaymentDto {

    @Schema(description = "Unique ID of the payment",
            example = "1")
    private Long id;

    @NotBlank(message = "Amount cannot be blank")
    @Schema(description = "Total amount paid",
            example = "450.00")
    private Double amount;

    @Schema(description = "Date of the payment",
            example = "2024-01-01",
            required = true,
            hidden = true)
    private Date paymentDate;

    @NotBlank(message = "Payment method cannot be blank")
    @Schema(description = "Payment method used",
            example = "Credit Card",
            required = true)
    private String paymentMethod;

    @Schema(description = "Order associated with the payment")
    private OrderDto orderDto;


    @Schema(description = "The status of the payment", example = "PAID")
    private PaymentStatus paymentStatus;




}
