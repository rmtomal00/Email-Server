package link.team71.emailserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtTokenFilterResponse {
    private boolean error;
    private int code;
    private String message;

}
