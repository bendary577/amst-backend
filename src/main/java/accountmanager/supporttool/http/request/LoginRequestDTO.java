package accountmanager.supporttool.http.request;


public class LoginRequestDTO {
    private String email;
    private String password;

    //need default constructor for JSON Parsing
    public LoginRequestDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
