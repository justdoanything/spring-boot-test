package yong.constants;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class HttpUrlConstant {

    public static final Map<HttpMethod, List<String>> NO_AUTH_SESSION_HTTP_URL = new EnumMap<>(HttpMethod.class);

    static {
        String[] getExcludeUrl = {
                "/v1/get/exclude/url"
        };

        String[] postExcludeUrl = {
                "/v1/post/exclude/url"
        };

        String[] putExcludeUrl = {
                "/v1/put/exclude/url"
        };

        String[] patchExcludeUrl = {
                "/v1/patch/exclude/url"
        };

        String[] deleteExcludeUrl = {
                "/v1/delete/exclude/url"
        };

        NO_AUTH_SESSION_HTTP_URL.put(HttpMethod.GET, new ArrayList<>());
        NO_AUTH_SESSION_HTTP_URL.put(HttpMethod.POST, new ArrayList<>());
        NO_AUTH_SESSION_HTTP_URL.put(HttpMethod.PUT, new ArrayList<>());
        NO_AUTH_SESSION_HTTP_URL.put(HttpMethod.PATCH, new ArrayList<>());
        NO_AUTH_SESSION_HTTP_URL.put(HttpMethod.DELETE, new ArrayList<>());

        addExcludeHttpPathUrl(HttpMethod.GET, getExcludeUrl);
        addExcludeHttpPathUrl(HttpMethod.POST, postExcludeUrl);
        addExcludeHttpPathUrl(HttpMethod.PUT, putExcludeUrl);
        addExcludeHttpPathUrl(HttpMethod.PATCH, patchExcludeUrl);
        addExcludeHttpPathUrl(HttpMethod.DELETE, deleteExcludeUrl);
    }

    private static void addExcludeHttpPathUrl(HttpMethod httpMethod, String[] patternList) {
        List<String> urlList = NO_AUTH_SESSION_HTTP_URL.get(httpMethod);
        Collections.addAll(urlList, patternList);
    }
}
