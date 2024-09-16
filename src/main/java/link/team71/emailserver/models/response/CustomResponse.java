package link.team71.emailserver.models.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomResponse {

    public Map successWithData(Object data) {
        Map<String, Object> newData = new HashMap<>();
        newData.put("message", "success");
        newData.put("error", false);
        newData.put("data", data);
        newData.put("code", HttpStatus.OK.value());
        return newData;
    }
    public Map successWithoutData() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "success");
        result.put("code", HttpStatus.OK.value());
        result.put("error", false);
        return result;
    }

    public Map error(String message, int code) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("code", code);
        result.put("error", true);
        return result;
    }

    public Map serverError(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put("error", true);
        return result;
    }
}
