package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order Update Request DTO is used for transferring updated order data across the application.")
public class OrderUpdateRequest {

    @Schema(description = "Updated order details",
            example = "{id: 1, orderDate: '2024-01-01', totalAmount: 450.00, checkInDate: '2024-10-12', checkOutDate: '2024-10-14'}")
    private OrderDto orderDto;

    @Schema(description = "Updated payment details",
            example = "{paymentStatus: 'PAID', amount: 450.00}")
    private PaymentDto paymentDto;




}
