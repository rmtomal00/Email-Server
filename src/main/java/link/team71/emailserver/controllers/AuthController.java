package link.team71.emailserver.controllers;

import link.team71.emailserver.models.AuthModels.Login;
import link.team71.emailserver.models.AuthModels.RegisterUser;
import link.team71.emailserver.models.response.CustomResponse;
import link.team71.emailserver.repos.Register;
import link.team71.emailserver.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(("/api/v1/auth"))
public class AuthController {
    @Autowired
    private Register registerService;
    @Autowired
    private AuthService authService;

    @Autowired
    private CustomResponse customResponse;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUser user) {
        try {

            if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse.error("Password is empty or length less then 6", 400));
            }

            if(authService.registerUser(user) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse.error("Email or Password invalid", 400));
            }
            return ResponseEntity.status(HttpStatus.OK).body(customResponse.successWithoutData());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login user) {
        try{
            Map res =  authService.login(user.getUsername(), user.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse().error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
