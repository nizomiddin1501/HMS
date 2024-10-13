package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "check_in_date", nullable = false)
    @Schema(description = "The date the user plans to check in",
            example = "2024-10-12",
            required = true)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    @Schema(description = "The date the user plans to check out",
            example = "2024-10-14",
            required = true)
    private LocalDate checkOutDate;






}
