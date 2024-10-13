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
@Table(name = "roles")
@Schema(description = "Role entity represents the different roles available for users.")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    @Schema(description = "Name of the role", example = "ADMIN", required = true)
    private String name;













}
