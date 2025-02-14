package com.project.InsureCompare.util;

import java.util.Map;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Component
public class EntityUpdater {
	public <T> void updateEntityFields(T entity, Map<String, Object> updateRequest) {
        updateRequest.forEach((fieldName, newValue) -> {
            try {
                Field field = entity.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(entity, newValue);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Invalid field: " + fieldName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + fieldName, e);
            }
        });
    }
}
