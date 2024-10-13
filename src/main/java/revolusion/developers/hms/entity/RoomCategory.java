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
@Table(name = "room_category")
@Schema(description = "RoomCategory entity represents the category and pricing information for hotel rooms.")
public class RoomCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true)
    @Schema(description = "Name of the room category",
            example = "lux",
            required = true)
    private String categoryName;

    @Column(name = "price", nullable = false)
    @Schema(description = "Price for the room category",
            example = "150.00",
            required = true)
    private Double price;

}
