package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

class RuntimeTypeAdapterFactory_47_2Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withValidTypeAndLabel_shouldRegisterSuccessfully() {
    RuntimeTypeAdapterFactory<Object> result = factory.registerSubtype(String.class, "string");
    assertSame(factory, result);

    // Using reflection to verify private fields
    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey("string"));
      assertEquals(String.class, labelToSubtype.get("string"));

      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(String.class));
      assertEquals("string", subtypeToLabel.get(String.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withNullType_shouldThrowNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(null, "label");
    });
    assertNull(exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withNullLabel_shouldThrowNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(String.class, null);
    });
    assertNull(exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withDuplicateType_shouldThrowIllegalArgumentException() {
    factory.registerSubtype(String.class, "string");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(String.class, "anotherLabel");
    });
    assertEquals("types and labels must be unique", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withDuplicateLabel_shouldThrowIllegalArgumentException() {
    factory.registerSubtype(String.class, "string");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Integer.class, "string");
    });
    assertEquals("types and labels must be unique", exception.getMessage());
  }
}