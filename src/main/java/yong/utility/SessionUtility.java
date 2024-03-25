package yong.utility;

import lombok.experimental.UtilityClass;
import yong.model.SessionVO;

@UtilityClass
public class SessionUtility {
    public SessionVO getSessionVO(String sessionId) {
        return SessionVO.builder().build();
    }
}
