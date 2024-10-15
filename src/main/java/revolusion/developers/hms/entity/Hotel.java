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
@Table(name = "hotel")
@Schema(description = "Hotel entity represents a hotel in the system.")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "hotel_name", length = 50, nullable = false)
    @Schema(description = "Name of the hotel",
            example = "Hyatt",
            required = true)
    private String name;

    @Column(name = "address", length = 100, nullable = false)
    @Schema(description = "Address of the hotel",
            example = "123 Main Street, City, Country",
            required = true)
    private String address;

    @Column(name = "star_rating", nullable = false)
    @Schema(description = "Star rating of the hotel",
            example = "5",
            required = true)
    private Integer starRating;


    @Column(name = "description", nullable = false)
    @Schema(description = "Brief description about the hotel",
            example = "A luxury hotel located in the heart of the city.")
    private String description;

    @Column(name = "balance", nullable = false)
    @Schema(description = "The current balance of the hotel account.",
            example = "10000.00",
            required = true)
    private Double balance;

    @Column(name = "account_number", length = 20, nullable = false, unique = true)
    @Schema(description = "The account number of the hotel.",
            example = "ACCT123456",
            required = true)
    private String accountNumber;









}
