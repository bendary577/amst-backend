package accountmanager.supporttool.http.request;


import accountmanager.supporttool.enumeration.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import java.io.Serializable;

public class NewAppUserRequestDTO implements Serializable {

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    //need default constructor for JSON Parsing
    public NewAppUserRequestDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
