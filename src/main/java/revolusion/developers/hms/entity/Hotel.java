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
@Table(name = "hotels")
@Schema(description = "Hotel entity represents a hotel in the system.")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "hotel_name", length = 50, nullable = false)
    @Schema(description = "Name of the hotel",
            example = "Grand Hotel",
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







}
