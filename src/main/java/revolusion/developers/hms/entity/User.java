package revolusion.developers.hms.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Schema(description = "User entity represents a registered user in the system.")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the user",
            example = "1")
    private Long id;

    @Column(name = "user_name", length = 30, nullable = false)
    @Schema(description = "Name of the user",
            example = "Nizomiddin Mirzanazarov",
            required = true)
    private String name;

    @Column(name = "user_email", length = 30, nullable = false, unique = true)
    @Schema(description = "Email address of the user",
            example = "nizomiddin@gmail.com",
            required = true)
    private String email;

    @Column(name = "user_password", length = 50, nullable = false)
    @Schema(description = "Password of the user",
            example = "password123",
            required = true)
    private String password;

    @Column(name = "about", length = 100)
    @Schema(description = "Brief description about the user",
            example = "A frequent traveler who loves staying in luxury hotels.")
    private String about;

    @Column(name = "verification_code")
    @Schema(description = "Verification code for email confirmation")
    private String verificationCode;

    @Column(name = "reset_code")
    @Schema(description = "Code used for password reset")
    private String resetCode;

    @Column(name = "is_verified", nullable = false)
    @Schema(description = "Indicates whether the user's email is verified", example = "false")
    private boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Roles assigned to the user.")
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "List of payment information associated with the user.")
    private Set<UserPayment> userPayments;


    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

}
