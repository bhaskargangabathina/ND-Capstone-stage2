package in.capstone.newsapp.models;

/**
 * Created by bhaskar.gangabathina on 03-01-2017.
 */

public class TokenResponse {

    private Boolean error;
    private String error_msg;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return error_msg;
    }

    public void setMessage(String message) {
        this.error_msg = message;
    }

}
