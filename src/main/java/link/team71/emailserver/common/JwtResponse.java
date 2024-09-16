package link.team71.emailserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import link.team71.emailserver.models.JwtTokenFilterResponse;

import java.io.IOException;

public class JwtResponse {
    private HttpServletResponse response;
    private boolean error;
    private String msg;

    public JwtResponse(HttpServletResponse response, boolean error, String msg) {
        this.response = response;
        this.error = error;
        this.msg = msg;
    }

    public HttpServletResponse res() throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new JwtTokenFilterResponse(error, HttpServletResponse.SC_UNAUTHORIZED, msg)));
        return response;
    }
}
