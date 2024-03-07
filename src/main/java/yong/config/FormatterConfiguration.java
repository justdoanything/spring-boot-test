package yong.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class FormatterConfiguration implements WebMvcConfigurer {

    private final Formatter<?>[] formatters;

    private final Converter<?, ?>[] converters;

    private final ConverterFactory<?, ?>[] converterFactories;

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        if (!ObjectUtils.isEmpty(formatters)) {
            for (final Formatter<?> formatter : formatters){
                formatterRegistry.addFormatter(formatter);
            }
        }

        if (!ObjectUtils.isEmpty(converters)) {
            for (final Converter<?, ?> converter : converters){
                formatterRegistry.addConverter(converter);
            }
        }

        if (!ObjectUtils.isEmpty(converterFactories)) {
            for (final ConverterFactory<?, ?> converterFactory : converterFactories){
                formatterRegistry.addConverterFactory(converterFactory);
            }
        }
    }
}
