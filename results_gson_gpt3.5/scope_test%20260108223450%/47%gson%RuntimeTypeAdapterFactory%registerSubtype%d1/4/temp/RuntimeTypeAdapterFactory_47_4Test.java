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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

public class RuntimeTypeAdapterFactory_47_4Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  public void setUp() throws Exception {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_nullType_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(null, "label");
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_nullLabel_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(String.class, null);
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_duplicateType_throwsIllegalArgumentException() throws Exception {
    factory.registerSubtype(String.class, "stringLabel");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(String.class, "anotherLabel");
    });
    assertEquals("types and labels must be unique", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_duplicateLabel_throwsIllegalArgumentException() throws Exception {
    factory.registerSubtype(String.class, "stringLabel");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Integer.class, "stringLabel");
    });
    assertEquals("types and labels must be unique", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_successfulRegistration_addsToMaps() throws Exception {
    RuntimeTypeAdapterFactory<Object> testFactory = RuntimeTypeAdapterFactory.of(Object.class, "type");

    RuntimeTypeAdapterFactory<Object> returned = testFactory.registerSubtype(String.class, "stringLabel");

    assertSame(testFactory, returned);

    // Use reflection to access private fields
    Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(testFactory);
    assertTrue(labelToSubtype.containsKey("stringLabel"));
    assertEquals(String.class, labelToSubtype.get("stringLabel"));

    Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(testFactory);
    assertTrue(subtypeToLabel.containsKey(String.class));
    assertEquals("stringLabel", subtypeToLabel.get(String.class));
  }
}