package anhlv.auth.dto;

import org.antlr.v4.runtime.misc.NotNull;

public class UserRegisterDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String role;

    public UserRegisterDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
