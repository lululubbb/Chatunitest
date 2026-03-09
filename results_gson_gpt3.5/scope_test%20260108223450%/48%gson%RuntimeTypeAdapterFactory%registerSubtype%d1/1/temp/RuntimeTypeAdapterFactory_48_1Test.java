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
import static org.mockito.Mockito.*;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RuntimeTypeAdapterFactory_48_1Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withClass_registersWithSimpleNameLabel() {
    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(String.class);
    assertSame(factory, returned);

    // Use reflection to verify private fields labelToSubtype and subtypeToLabel
    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey("String"));
      assertEquals(String.class, labelToSubtype.get("String"));

      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(String.class));
      assertEquals("String", subtypeToLabel.get(String.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withNullType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> factory.registerSubtype(null));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withDuplicateType_throwsIllegalArgumentException() {
    factory.registerSubtype(String.class);

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> factory.registerSubtype(String.class));
    String msg = e.getMessage().toLowerCase();
    assertTrue(msg.contains("label \"string\" already registered".toLowerCase())
        || msg.contains("already registered"));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withTypeHavingEmptySimpleName_usesEmptyStringLabel() {
    // Create anonymous class with empty simple name by reflection trick
    Class<?> anonymousClass = new Object() {}.getClass();

    RuntimeTypeAdapterFactory<Object> f = RuntimeTypeAdapterFactory.of(Object.class);

    RuntimeTypeAdapterFactory<Object> returned = f.registerSubtype(anonymousClass);
    assertSame(f, returned);

    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(f);
      // The label should be the empty string "" if simpleName is empty
      String expectedLabel = anonymousClass.getSimpleName();
      if (expectedLabel.isEmpty()) {
        expectedLabel = "";
      }
      assertTrue(labelToSubtype.containsKey(expectedLabel));
      assertEquals(anonymousClass, labelToSubtype.get(expectedLabel));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}