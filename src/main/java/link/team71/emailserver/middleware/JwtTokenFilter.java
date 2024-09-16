package link.team71.emailserver.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import link.team71.emailserver.common.JwtResponse;
import link.team71.emailserver.models.User;
import link.team71.emailserver.repos.Register;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private Register register;

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        try{
            String authorizationHeader = request.getHeader("Authorization");



            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                if (request.getRequestURI().contains("/api/v1/auth")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                System.out.println(request.getRequestURI());
                System.out.println("Authorization header not found");
                new JwtResponse(response, true, "Missing Jwt Token").res();
                //filterChain.doFilter(request, response);
                return;
            }
            String[] token = authorizationHeader.split(" ");
            String userJwt = token[1].trim();
            if (userJwt.isBlank()) {

                new JwtResponse(response, true, "Invalid Jwt Token").res();
                //filterChain.doFilter(request, response);
                return;
            }

            Map<String, Object> userTokenExtract = jwtGenerator.verifyJwt(userJwt);

            long id = Long.parseLong(userTokenExtract.get("id").toString());
            if (id == 0 ) {
                new JwtResponse(response, true, "Invalid Jwt Token").res();
                //filterChain.doFilter(request, response);
                return;
            }
            //System.out.println(userTokenExtract);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = register.findById(id).orElse(null);
                assert user != null;
                UserDetails userDetails = applicationContext.getBean(UserDetailsProvider.class).loadUserByUsername(user.getUsername());
                if (jwtGenerator.validateJwt(userJwt, user.getToken())) {
                    System.out.println(userJwt);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        }catch (Exception e){
            System.out.println(e.getMessage());
            new JwtResponse(response, true, e.getMessage()).res();
            return;
            //filterChain.doFilter(request, response);
        }

    }
}
