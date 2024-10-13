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
    @Schema(hidden = true)
    private Long id;

    @Column(name = "user_name", length = 20, nullable = false)
    @Schema(description = "Name of the user",
            example = "Nizomiddin Mirzanazarov",
            required = true)
    private String name;

    @Column(name = "user_email", length = 30, nullable = false, unique = true)
    @Schema(description = "Email address of the user",
            example = "johndoe@example.com",
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


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Roles assigned to the user.")
    private Set<Role> roles;











}
