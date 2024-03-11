package yong.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.Arrays;

public class EnumDeserializer extends StdDeserializer<Enum <? extends Enum>> implements ContextualDeserializer {

    public EnumDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Enum<? extends Enum> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Class<? extends Enum> enumType = (Class<? extends Enum>) this._valueClass;
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String input = jsonNode.asText().trim().toUpperCase();

        if(ObjectUtils.isEmpty(input))
            throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

        boolean isPlainEnum = EnumUtils.isValidEnum(enumType, input);

        if(isPlainEnum){
            return Enum.valueOf(enumType, input);
        } else {
            boolean isEnumCode = Arrays.stream(enumType.getMethods()).anyMatch(method -> "code".equals(method.getName()));

            if(isEnumCode){
                Enum mathcEnum = null;
                String enumValue;
                for(Enum constant : enumType.getEnumConstants()){
                    try {
                        enumValue = (String) constant.getClass().getMethod("code").invoke(constant);
                        if(enumValue.equals(input.trim().toUpperCase())){
                            mathcEnum = constant;
                            break;
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
                    }
                }

                if(mathcEnum == null)
                    throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");

                return Enum.valueOf(enumType,mathcEnum.name());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ContentsTypeCode 입니다.");
            }
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty beanProperty) {
        return new EnumDeserializer(beanProperty.getType().getRawClass());
    }
}
