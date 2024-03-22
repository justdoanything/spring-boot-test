package yong.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import yong.annotation.RequestBind;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestBindResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBind.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory) throws Exception {
        Map<String, String> requestParameters =
                nativeWebRequest.getParameterMap()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        if(requestParameters.size() == 0) {
            RequestBind requestBind = methodParameter.getParameterAnnotation(RequestBind.class);
            if(requestBind != null && requestBind.required()) {
                HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
                ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(httpServletRequest);
                throw new HttpMessageNotReadableException(
                        "Required request parameter is empty : " + methodParameter.getExecutable().toGenericString(),
                        servletServerHttpRequest);
            } else {
                // 검증 로직 추가
            }
        }

        Object resolver = mapper.convertValue(requestParameters, methodParameter.getParameterType());

        if(methodParameter.hasParameterAnnotation(Valid.class)) {
            String parameterName = Conventions.getVariableNameForParameter(methodParameter);
            WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, resolver, parameterName);
            if(binder.getTarget() != null) {
                binder.validate();
                BindingResult bindingResult = binder.getBindingResult();
                if(bindingResult.hasErrors()) {
                    throw new MethodArgumentNotValidException(methodParameter, bindingResult);
                }
            }
        }

        return resolver;
    }
}
