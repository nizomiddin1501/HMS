package revolusion.developers.hms.payload;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO is used for transferring user data across the application.")
public class UserDto {

    @Schema(description = "Unique ID of the user",
            example = "1")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 30, message = "Name must be less than or equal to 30 characters")
    @Schema(description = "Name of the user",
            example = "Nizomiddin Mirzanazarov",
            required = true)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user",
            example = "nizomiddin@gmail.com",
            required = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Schema(description = "Password of the user",
            example = "password123",
            required = true)
    private String password;

    @Schema(description = "Brief description about the user",
            example = "A regular hotel customer.")
    private String about;

    @Schema(description = "Verification code sent to the user's email for verification.")
    private String verificationCode;

    @Schema(description = "Reset code sent to the user's email for password reset.")
    private String resetCode;

    @Schema(description = "Indicates whether the user's email is verified or not.")
    private boolean isVerified;

    @JsonIgnore
    @Schema(hidden = true,
            description = "Time when the verification or reset code expires")
    private LocalDateTime codeExpiryTime;


    @Schema(description = "Roles assigned to the user.")
  //          example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<RoleDto> roles;

}
