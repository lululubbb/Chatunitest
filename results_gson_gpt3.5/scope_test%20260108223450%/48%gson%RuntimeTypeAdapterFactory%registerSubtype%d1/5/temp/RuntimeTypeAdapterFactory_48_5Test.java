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

class RuntimeTypeAdapterFactory_48_5Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withClassOnly_registersWithSimpleName() {
    class SubType {}
    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(SubType.class);
    assertSame(factory, returned);

    // Use reflection to access private fields
    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);

      assertTrue(labelToSubtype.containsKey("SubType"));
      assertEquals(SubType.class, labelToSubtype.get("SubType"));
      assertTrue(subtypeToLabel.containsKey(SubType.class));
      assertEquals("SubType", subtypeToLabel.get(SubType.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withNullType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> factory.registerSubtype(null));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_registersMultipleSubtypes_correctly() {
    class SubTypeA {}
    class SubTypeB {}

    factory.registerSubtype(SubTypeA.class);
    factory.registerSubtype(SubTypeB.class);

    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);

      assertEquals(2, labelToSubtype.size());
      assertEquals(2, subtypeToLabel.size());

      assertEquals(SubTypeA.class, labelToSubtype.get("SubTypeA"));
      assertEquals(SubTypeB.class, labelToSubtype.get("SubTypeB"));
      assertEquals("SubTypeA", subtypeToLabel.get(SubTypeA.class));
      assertEquals("SubTypeB", subtypeToLabel.get(SubTypeB.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }
}