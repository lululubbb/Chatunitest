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

class RuntimeTypeAdapterFactory_48_6Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withClass_registersSubtypeWithSimpleNameLabel() {
    class Subtype {}

    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(Subtype.class);

    assertSame(factory, returned);
    // Use reflection to verify private maps contain the subtype and label
    try {
      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(Subtype.class));
      assertEquals("Subtype", subtypeToLabel.get(Subtype.class));

      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey("Subtype"));
      assertEquals(Subtype.class, labelToSubtype.get("Subtype"));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }
}