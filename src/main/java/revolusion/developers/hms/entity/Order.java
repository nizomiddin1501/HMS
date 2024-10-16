package revolusion.developers.hms.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolusion.developers.hms.entity.status.OrderStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Schema(description = "Order entity represents a booking made by the user.")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "order_date", nullable = false)
    @Schema(description = "The date the order was placed",
            example = "2024-10-10",
            required = true)
    private LocalDate orderDate;

    @Column(name = "order_amount", nullable = false)
    @Schema(description = "Total amount of the order",
            example = "450.00")
    private Double totalAmount;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    @Schema(description = "The status of the order", example = "PENDING")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user who placed the order")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @Schema(description = "The room that was booked")
    private Room room;


    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDate.now();
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.PENDING;
        }
    }













}
