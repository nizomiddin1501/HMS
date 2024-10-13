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
@Table(name = "room")
@Schema(description = "Room entity represents rooms in the hotel.")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "room_number", length = 10, nullable = false, unique = true)
    @Schema(description = "Room number",
            example = "101",
            required = true)
    private String roomNumber;

    @Column(name = "category", nullable = false)
    @Schema(description = "Category of the room",
            example = "lux",
            required = true)
    private String category;

    @Column(name = "price", nullable = false)
    @Schema(description = "Price of the room per night",
            example = "150.00",
            required = true)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @Schema(description = "The hotel that the room belongs to")
    private Hotel hotel;











}
