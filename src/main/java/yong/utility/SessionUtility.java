package yong.utility;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import yong.model.SessionVO;

@UtilityClass
public class SessionUtility {
    public SessionVO getSessionVO(String sessionId) {
        return SessionVO.builder().build();
    }

    public void setContextSession(String name, Object value) {
        RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
    }

    public void setContextSession(SessionVO sessionUser) {
        setContextSession("session", sessionUser);
    }
}
