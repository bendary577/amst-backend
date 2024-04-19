package accountmanager.supporttool.model.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AppUser",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    @NotBlank(message = "name is mandatory")
    @Size(max = 50)
    @Email
    private String name;

    @Column(name = "email")
    @NotBlank(message = "email is mandatory")
    @Size(max = 50)
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank(message = "password is mandatory")
    @Size(max = 120)
    private String password;

    @Column(name = "isBlocked")
    private boolean isBlocked;

    public AppUser(){}

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
