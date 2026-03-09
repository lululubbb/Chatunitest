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

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

class RuntimeTypeAdapterFactory_48_4Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withClass_shouldRegisterWithSimpleNameLabel() {
    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(String.class);

    assertSame(factory, returned);

    // Use reflection to verify private fields labelToSubtype and subtypeToLabel
    try {
      Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey("String"));
      assertEquals(String.class, labelToSubtype.get("String"));

      Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(String.class));
      assertEquals("String", subtypeToLabel.get(String.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withAnonymousClass_shouldRegisterWithSimpleNameLabel() {
    Object anon = new Object() {};
    Class<?> anonClass = anon.getClass();

    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(anonClass);

    assertSame(factory, returned);

    try {
      Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey(anonClass.getSimpleName()));
      assertEquals(anonClass, labelToSubtype.get(anonClass.getSimpleName()));

      Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(anonClass));
      assertEquals(anonClass.getSimpleName(), subtypeToLabel.get(anonClass));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateLabel_shouldThrow() {
    factory.registerSubtype(String.class);
    // Registering subtype with same label (String) again should throw
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Integer.class, "String");
    });
    assertTrue(thrown.getMessage().toLowerCase().contains("already registered"));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateClass_shouldThrow() {
    factory.registerSubtype(String.class);

    // Use reflection to add a conflicting label with same class to simulate
    try {
      Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      subtypeToLabelField.setAccessible(true);
      labelToSubtypeField.setAccessible(true);
      Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
      Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);

      // Remove old label for String.class to avoid conflict with labelToSubtype map
      String oldLabel = subtypeToLabel.get(String.class);
      if (oldLabel != null) {
        labelToSubtype.remove(oldLabel);
      }

      // Put conflicting label pointing to the same class
      subtypeToLabel.put(String.class, "OtherLabel");
      labelToSubtype.put("OtherLabel", String.class);

      // Now registering String.class again with any label should throw
      IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        factory.registerSubtype(String.class, "OtherLabel");
      });
      assertTrue(thrown.getMessage().toLowerCase().contains("already registered"));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}