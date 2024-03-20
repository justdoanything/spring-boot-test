package yong.aspect;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yong.exception.BusinessException;

import java.util.Set;

@Aspect
@Component
public class ResponseAspect {
    @Autowired
    private Validator validator;

    @AfterReturning(pointcut = "execution(* yong.controller..*(..))", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) throws BusinessException {
        validateResponse(response);
    }

    private void validateResponse(Object object) throws BusinessException {

        Set<ConstraintViolation<Object>> validationResults = validator.validate(object);

        if (validationResults.size() > 0) {

            StringBuffer sb = new StringBuffer();

            for (ConstraintViolation<Object> error : validationResults) {
                sb.append(error.getPropertyPath())
                        .append(" - ")
                        .append(error.getMessage())
                        .append("\n");
            }

            String msg = sb.toString();
            throw new BusinessException(msg);
        }
    }
}
