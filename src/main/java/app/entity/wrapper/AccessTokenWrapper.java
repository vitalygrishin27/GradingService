package app.entity.wrapper;

import app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessTokenWrapper {
    private String token;
    private String login;
    private Role role;
}
