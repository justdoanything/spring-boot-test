package yong.interceptor;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import yong.constants.HttpHeaderConstants;
import yong.constants.HttpUrlConstant;
import yong.exception.BusinessException;
import yong.model.SessionVO;
import yong.utility.SessionUtility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${token.connect.timeout}")
    private final int TOKEN_CONNECT_TIMEOUT;

    @Value("${token.read.timeout}")
    private final int TOKEN_READ_TIMEOUT;

    @Value("${cloud.aws.cognito.client-id}")
    private final String CLIENT_ID;

    @Value("${cloud.aws.cognito.region}")
    private final String REGION;

    @Value("${cloud.aws.cognito.user-pool-id}")
    private final String USER_POOL_ID;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String sessionId = request.getHeader(HttpHeaderConstants.SESSION_ID);
        String authorization = request.getHeader(HttpHeaderConstants.AUTHORIZATION);

        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        } else if (isExcludePattern(HttpMethod.valueOf(request.getMethod()), request.getRequestURI())) {
            if (!ObjectUtils.isEmpty(sessionId)) {
                if (ObjectUtils.isEmpty(authorization)) {
                    return true;
                }

                if (Pattern.matches("^Bearer .*", authorization)) {
                    authorization = authorization.replaceAll("^Bearer( )*", "");
                }

                SessionVO sessionUser = SessionUtility.getSessionVO(sessionId);
                if (sessionUser != null && this.verifyToken(authorization)) {
                    SessionUtility.setContextSession(sessionUser);
                }
            }
            return true;
        } else {
            if (ObjectUtils.isEmpty(sessionId) || ObjectUtils.isEmpty(authorization)) {
                throw new BusinessException("Authorization is required.");
            }

            if (Pattern.matches("^Bearer .*", authorization)) {
                authorization = authorization.replaceAll("^Bearer( )*", "");
            }

            if (!this.verifyToken(authorization)) {
                throw new BusinessException("Session is unauthorized.");
            }

            SessionVO sessionUser = SessionUtility.getSessionVO(sessionId);
            if (sessionUser == null) {
                throw new BusinessException("Session is expired.");
            }

            SessionUtility.setContextSession(sessionUser);
            return true;
        }
    }

    private boolean isExcludePattern(HttpMethod httpMethod, String requestUri) {
        List<String> uriList = HttpUrlConstant.NO_AUTH_SESSION_HTTP_URL.get(httpMethod);
        return uriList.stream().anyMatch(uri -> antPathMatcher.match(uri, requestUri));
    }

    private boolean verifyToken(String token) {
        try {
            JWTClaimsSet jwtClaimsSet = configurableJWTProcessor().process(token, null);
            String cognitoPoolUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, USER_POOL_ID);
            if (!jwtClaimsSet.getIssuer().equals(cognitoPoolUrl)
                    || !jwtClaimsSet.getAudience().get(0).equals(CLIENT_ID)
                    || !jwtClaimsSet.getClaim("token_use").equals("id")) {
                return false;
            }

            Date now = new Date(System.currentTimeMillis());
            if (!now.before(jwtClaimsSet.getExpirationTime())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
        ResourceRetriever resourceRetriever = new DefaultResourceRetriever(TOKEN_CONNECT_TIMEOUT, TOKEN_READ_TIMEOUT);

        URL url = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, USER_POOL_ID));

        JWKSource jwkSource = new RemoteJWKSet(url, resourceRetriever);

        ConfigurableJWTProcessor configurableJWTProcessor = new DefaultJWTProcessor();
        JWSKeySelector jwsKeySelector = new JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSource);

        configurableJWTProcessor.setJWSKeySelector(jwsKeySelector);
        return configurableJWTProcessor;
    }
}
