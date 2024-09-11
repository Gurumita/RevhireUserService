package com.revhire.userservice.utilities;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
@Component
public class ModelUpdater {
    public  <T> T updateFields(T existing, T details) {
        Field[] fields = details.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                Object value = field.get(details);

                if (value != null) {
                    if (isNumericField(field.getType())) {
                        if (!isZero(value)) {
                            field.set(existing, value);
                        }
                    } else {
                        field.set(existing, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }

        return existing;
    }

    private  boolean isNumericField(Class<?> fieldType) {
        return fieldType == int.class || fieldType == long.class || fieldType == float.class || fieldType == double.class;
    }

    private  boolean isZero(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue() == 0.0;
        }
        return false;
    }
}
