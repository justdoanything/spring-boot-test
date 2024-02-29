package yong.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import yong.annotation.Enum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<Enum, String> {
    private List<String> enumNames;
    private List<String> enumCodes;
    private Enum annotation;

    @Override
    public void initialize(Enum value) {
        this.annotation = value;

        List<String> excludeEnumType =
                Arrays.stream(this.annotation.excludeEnumType()).toList();

        enumNames = Arrays.stream(this.annotation.enumClass().getEnumConstants())
                .map(constants ->
                        this.annotation.ignoreCase() ? constants.name().toUpperCase() : constants.name())
                .filter(constants -> !excludeEnumType.contains(constants))
                .collect(Collectors.toList());

        boolean isHaveCodeMethod = Arrays.stream(value.enumClass().getMethods()).anyMatch(method -> "code".equals(method.getName()));

        if (isHaveCodeMethod) {
            enumCodes = Arrays.stream(this.annotation.enumClass().getEnumConstants()).map(constant -> {
                try {
                    return (String) constant.getClass().getMethod("code").invoke(constant);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } else {
            enumCodes = Collections.emptyList();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            value = this.annotation.ignoreCase() ? value.toUpperCase() : value;
            return enumNames.contains(value) || enumCodes.contains(value);
        }
    }
}
