package yong.converter;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> enumType) {
        return new EnumConverter(getEnumType(enumType));
    }

    private Class<?> getEnumType(Class classType) {
        Class<?> enumType = classType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        if (enumType == null) {
            throw new IllegalArgumentException("This type " + enumType.getName() + " is not an enum type.");
        }
        return enumType;
    }

    private class EnumConverter<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        private EnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String input) {
            System.out.println("Converter input : " + input);
            input = input.trim().toUpperCase();

            if(ObjectUtils.isEmpty(input))
                throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

            boolean isPlainEnum = EnumUtils.isValidEnum(enumType, input);

            if (isPlainEnum) {
                return (T) Enum.valueOf(this.enumType, input);
            } else {
                boolean isEnumCode = Arrays.stream(enumType.getMethods()).anyMatch(method -> "code".equals(method.getName()));

                if (isEnumCode) {
                    Enum mathcEnum = null;
                    String enumValue;
                    for (Enum constant : enumType.getEnumConstants()) {
                        try {
                            enumValue = (String) constant.getClass().getMethod("code").invoke(constant);
                            if (enumValue.equals(input.trim().toUpperCase())) {
                                mathcEnum = constant;
                                break;
                            }
                        } catch (Exception e) {
                            throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
                        }
                    }

                    if (mathcEnum == null)
                        throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

                    return (T) Enum.valueOf(enumType, mathcEnum.name());
                } else {
                    throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
                }
            }
        }
    }
}