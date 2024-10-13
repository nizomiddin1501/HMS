package revolusion.developers.hms.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "status", nullable = false)
    @Schema(description = "Status of the order",
            example = "Confirmed",
            required = true)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user who placed the order")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @Schema(description = "The room that was booked")
    private Room room;













}
