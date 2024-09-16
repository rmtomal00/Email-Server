package link.team71.emailserver.models.AuthModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String username;
    private String password;
    private String email;
    private String phone;
}
