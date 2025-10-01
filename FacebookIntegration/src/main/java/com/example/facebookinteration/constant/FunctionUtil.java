package com.example.facebookinteration.constant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule()); // Bỏ qua field không khớp
    // Chuyển Object thành JSON String
    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    // Chuyển JSON String thành Object
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    // Chuyển Map<String, Object> thành đối tượng
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz)  {
        return objectMapper.convertValue(map, clazz);
    }

    public static <S, T> T mapObject(S source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }

    public static OffsetDateTime convertToTimestamp(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return OffsetDateTime.parse(dateTimeStr, formatter);
    }
}
