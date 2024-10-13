package revolusion.developers.hms.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
@Schema(description = "Payment entity represents the payment for an order.")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "amount", nullable = false)
    @Schema(description = "The payment amount",
            example = "150.00",
            required = true)
    private Double amount;


    @Column(name = "payment_date", nullable = false)
    @Schema(description = "Date of the payment",
            example = "2024-01-01",
            hidden = true)
    private Date paymentDate;

    @Column(name = "payment_method", nullable = false)
    @Schema(description = "The method of payment",
            example = "Credit Card",
            required = true)
    private String paymentMethod;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "The order that the payment belongs to")
    private Order order;



    @PrePersist
    public void prePersist() {
        this.paymentDate = new Date(System.currentTimeMillis());
    }


}
