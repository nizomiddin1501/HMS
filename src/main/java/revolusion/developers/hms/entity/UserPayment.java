package revolusion.developers.hms.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_payments")
@Schema(description = "UserPayment entity represents the payment information of a user.")
public class UserPayment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "account_number", length = 30, nullable = false)
    @Schema(description = "Account number for the user's payment method",
            example = "1234567890",
            required = true)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    @Schema(description = "Current balance of the user's account",
            example = "1000.00",
            required = true)
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User associated with this payment information.")
    private User user;



}
