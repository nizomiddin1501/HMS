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
@Table(name = "reviews")
@Schema(description = "Review entity represents a user's review and rating of an order.")

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "rating", nullable = false)
    @Schema(description = "Rating given by the user",
            example = "4",
            required = true)
    private Integer rating;

    @Column(name = "comment", length = 500)
    @Schema(description = "Comment or review text given by the user",
            example = "Great room, had a fantastic stay!")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user who wrote the review")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "The order that the review is associated with")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @Schema(description = "The hotel that the review is related to")
    private Hotel hotel;






}
