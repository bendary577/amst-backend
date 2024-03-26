package accountmanager.supporttool.http.response;

import java.io.Serializable;
import java.util.List;

public class LoginResponseDTO extends ResponseMessage implements Serializable {
    private String email;
    private String token;
    private List<String> roles;

    //need default constructor for JSON Parsing
    public LoginResponseDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
