package yong.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ResolverConfiguration implements WebMvcConfigurer {

    private final HandlerMethodArgumentResolver[] handlerMethodArgumentResolvers;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if(!ObjectUtils.isEmpty(handlerMethodArgumentResolvers)) {
            resolvers.addAll(List.of(handlerMethodArgumentResolvers));
        }
    }
}
