package yong.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private static final String CLASS_LOG_FORMAT = "Class Name : [";
    private static final String METHOD_LOG_FORMAT = "Method Name : [";
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Before("(execution(* yong.exception..*(..))"
            + " || execution(* yong.controller..*(..)))"
            + " || execution(* yong.service..*(..)))"
            + " && !@annotation(* yong.annotation.NoLogging)")
    public void beforeMethod(final JoinPoint joinPoint) {
        HttpServletRequest request =
                ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes())
                        ? null
                        : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());

        StringBuilder logStringBuilder = new StringBuilder();
        String logInfo = logStringBuilder
                .append("원하는 LOG 내용")
                .toString();

        LOGGER.info("{}", logInfo);
    }

    @AfterReturning(
            pointcut = "(execution(* yong.exception..*(..))"
                    + " || execution(* yong.controller..*(..)))"
                    + " || execution(* yong.service..*(..)))"
                    + " && !@annotation(* yong.annotation.NoLogging)",
            returning = "result")
    public void afterMethod(final JoinPoint joinPoint, final Object result) {
        HttpServletRequest request =
                ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes())
                        ? null
                        : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();

        StringBuilder logStringBuilder = new StringBuilder();
        String logInfo = logStringBuilder
                .append("원하는 LOG 내용")
                .toString();

        LOGGER.info("{}", logInfo);
    }

    @AfterThrowing(
            pointcut = "(execution(* yong.exception..*(..))"
                    + " || execution(* yong.controller..*(..)))"
                    + " || execution(* yong.service..*(..)))"
                    + " && !@annotation(* yong.annotation.NoLogging)",
            throwing = "exception")
    public void afterThrowing(final JoinPoint joinPoint, final Exception exception) {
        HttpServletRequest request =
                ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes())
                        ? null
                        : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String exceptionMessage = exception.getMessage();

        StringBuilder logStringBuilder = new StringBuilder();
        String logInfo = logStringBuilder
                .append("원하는 LOG 내용")
                .toString();

        LOGGER.info("{}", logInfo);
    }
}
