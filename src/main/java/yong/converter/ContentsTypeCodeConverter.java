package yong.converter;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import yong.constants.ContentsTypeCode;

import java.util.Arrays;

public class ContentsTypeCodeConverter implements Converter<String, ContentsTypeCode> {
    Class<? extends Enum> enumClass = ContentsTypeCode.class;

    @Override
    public ContentsTypeCode convert(String source) {

        source = source.trim().toUpperCase();

        if (ObjectUtils.isEmpty(source))
            throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

        boolean isPlainEnum = EnumUtils.isValidEnum(enumClass, source);

        if (isPlainEnum) {
            return ContentsTypeCode.valueOf(source);
        } else {
            boolean isEnumCode = Arrays.stream(enumClass.getMethods()).anyMatch(method -> "code".equals(method.getName()));

            if (isEnumCode) {
                ContentsTypeCode matchedEnum = null;
                String enumCode;
                for (Enum constant : enumClass.getEnumConstants()) {
                    try {
                        enumCode = (String) constant.getClass().getMethod("code").invoke(constant);
                        if (enumCode.equals(source)) {
                            matchedEnum = (ContentsTypeCode) constant;
                            break;
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
                    }
                }

                if (matchedEnum == null)
                    throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

                return ContentsTypeCode.valueOf(matchedEnum.name());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
            }
        }
    }
}
