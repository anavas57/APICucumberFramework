package apiEngine.model.requests;

public class AuthorizationRequest {
    public String username;
    public String password;

    public AuthorizationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
