package link.team71.emailserver.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import link.team71.emailserver.models.response.CustomResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomResponseNotAuth implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \""+true+"\", \"message\": \""+ authException.getMessage() +"\", \"code\": 401}");
        response.getWriter().flush();
    }
}
