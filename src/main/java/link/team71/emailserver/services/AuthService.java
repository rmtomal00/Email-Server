package link.team71.emailserver.services;

import link.team71.emailserver.middleware.JwtGenerator;
import link.team71.emailserver.models.AuthModels.RegisterUser;
import link.team71.emailserver.models.User;
import link.team71.emailserver.models.enumRole.Role;
import link.team71.emailserver.models.response.CustomResponse;
import link.team71.emailserver.repos.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private Register registerService;
    @Autowired
    private JwtGenerator generator;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomResponse customResponse;

    public User getUser(String email) {
        return registerService.findByEmail(email.toLowerCase().trim());
    }

    public User registerUser(RegisterUser user) {
        if (user == null) {
            throw new IllegalStateException("Register service has not been initialized");
        }
        String hashPassword = new BCryptPasswordEncoder(10).encode(user.getPassword());
        user.setPassword(hashPassword);
        User u = User.builder()
                .email(user.getEmail().toLowerCase().trim())
                .role(Role.User)
                .password(user.getPassword())
                .username(user.getUsername())
                .phone(user.getPhone())
                .accountNonExpired(true)
                .isEnabled(true)
                .build();

        return registerService.save(u);
    }

    public Map login(String email, String password) {

        Map result = new HashMap<>();
        if (email == null || password == null) {
            throw new IllegalStateException("Login service has not been initialized");
        }

        User user = registerService.findByEmail(email.toLowerCase().trim());
        if (user == null) {
            throw new IllegalStateException("Email does not exist");
        }
        System.out.println(user.getPassword());
        System.out.println(new BCryptPasswordEncoder(10).encode(password));
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalStateException("Incorrect password");
        }

        String token = generator.generateJwt(new HashMap<String, Object>() {{
            put("email", user.getEmail());
            put("id", user.getId());
        }}, user.getUsername());

        user.setToken(token);
        registerService.save(user);
        Map <String, Object> map = new HashMap<>();
        map.put("token", token);
        result = customResponse.successWithData(map);
        return result;
    }

}
