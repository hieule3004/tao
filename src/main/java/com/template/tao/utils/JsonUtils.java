package com.template.tao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

  public static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new VavrModule())
      .registerModule(new JavaTimeModule()
          .addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
          .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

  public static <T> Try<T> readUrl(String url, Class<T> clz) {
    return Try.of(() -> MAPPER.readValue(new URL(url), clz));
  }

  @Deprecated
  public static String writeAsString(@NonNull Object object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return object.getClass().isArray() || object instanceof Collection ? "[]" : "{}";
    }
  }
}
