package revolusion.developers.hms.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolusion.developers.hms.entity.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order DTO is used for transferring order data across the application.")
public class OrderDto {

    @Schema(description = "Unique ID of the order",
            example = "1")
    private Long id;

    @Schema(description = "Date of the order",
            example = "2024-01-01")
    private LocalDate orderDate;

    @NotNull(message = "Total amount cannot be null")
    @Schema(description = "Total amount of the order",
            example = "450.00")
    private Double totalAmount;

    @NotNull(message = "Check-in date cannot be null")
    @Column(name = "check_in_date", nullable = false)
    @Schema(description = "The date the user plans to check in",
            example = "2024-10-12",
            required = true)
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @Column(name = "check_out_date", nullable = false)
    @Schema(description = "The date the user plans to check out",
            example = "2024-10-14",
            required = true)
    private LocalDate checkOutDate;

    @Schema(description = "The status of the order", example = "PENDING")
    private OrderStatus orderStatus;

    @Schema(description = "The deadline for the order, indicating the last date to complete the booking.",
            example = "2024-10-11",
            hidden = true)
    @JsonIgnore
    private LocalDateTime deadline;

    @Schema(description = "Room associated with the order")
    private RoomDto roomDto;

    @NotNull(message = "User is required for the order.")
    @Schema(description = "User who placed the order")
    private UserDto userDto;

}
