package apiEngine.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"code", "message"})
public class TokenResponse {
    public String token;
    public String expires;
    public String status;
    public String result;
}
