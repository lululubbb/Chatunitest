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

class RuntimeTypeAdapterFactory_47_1Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  void registerSubtype_nullType_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(null, "label");
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_nullLabel_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(Object.class, null);
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateType_throwsIllegalArgumentException() {
    factory.registerSubtype(String.class, "string");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(String.class, "string2");
    });
    assertEquals("types and labels must be unique", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateLabel_throwsIllegalArgumentException() {
    factory.registerSubtype(String.class, "label");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Integer.class, "label");
    });
    assertEquals("types and labels must be unique", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_successfulRegistration_returnsThisAndStoresMappings() throws Exception {
    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(String.class, "stringLabel");
    assertSame(factory, returned);

    // Use reflection to check private fields
    var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
    assertEquals(String.class, labelToSubtype.get("stringLabel"));

    var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
    assertEquals("stringLabel", subtypeToLabel.get(String.class));
  }
}