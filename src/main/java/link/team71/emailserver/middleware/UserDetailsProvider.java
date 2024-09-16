package link.team71.emailserver.middleware;

import link.team71.emailserver.models.User;
import link.team71.emailserver.repos.Register;
import link.team71.emailserver.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsProvider implements UserDetailsService {

    @Autowired
    Register register;


    public User getUserDetails() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        UserDetails userDetails = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        }



        return (User) userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userDetails = register.findByEmail(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        if (!userDetails.isEnabled()) {
            throw new UsernameNotFoundException(String.format("User %s is disabled", username));
        }
        return userDetails;
    }
}
