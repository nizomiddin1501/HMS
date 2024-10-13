package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order DTO is used for transferring order data across the application.")
public class OrderDto {


    @Schema(description = "Unique ID of the order", example = "1", hidden = true)
    private Long id;

    @Schema(description = "Date of the order",
            example = "2024-01-01")
    private LocalDate orderDate;

    @Schema(description = "Total amount of the order",
            example = "450.00")
    private Double totalAmount;

    @NotBlank(message = "Status cannot be blank")
    @Schema(description = "Status of the order",
            example = "Confirmed")
    private String status;

    @Schema(description = "Room associated with the order")
    private RoomDto roomDto;

    @Schema(description = "User who placed the order")
    private UserDto userDto;






}
