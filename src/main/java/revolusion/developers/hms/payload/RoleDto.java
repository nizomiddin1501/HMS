package revolusion.developers.hms.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role DTO is used for transferring role data across the application.")
public class RoleDto {

    @Schema(description = "Unique ID of the role",
            example = "1")
    private Long id;

    @NotBlank(message = "Role name cannot be blank")
    @Schema(description = "Name of the role",
            example = "ADMIN",
            required = true)
    private String name;









}
