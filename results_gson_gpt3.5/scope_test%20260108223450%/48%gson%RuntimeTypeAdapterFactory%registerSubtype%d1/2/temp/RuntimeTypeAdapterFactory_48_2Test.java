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

class RuntimeTypeAdapterFactory_48_2Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withClassOnly_registersWithSimpleNameLabel() {
    class SubType {}

    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(SubType.class);

    assertSame(factory, returned);

    // Use reflection to access private fields labelToSubtype and subtypeToLabel
    try {
      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(labelToSubtype.containsKey("SubType"));
      assertEquals(SubType.class, labelToSubtype.get("SubType"));

      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(subtypeToLabel.containsKey(SubType.class));
      assertEquals("SubType", subtypeToLabel.get(SubType.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withDuplicateLabel_throwsIllegalArgumentException() {
    class SubType1 {}
    class SubType2 {}

    factory.registerSubtype(SubType1.class, "label");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> factory.registerSubtype(SubType2.class, "label"));
    assertTrue(thrown.getMessage().toLowerCase().contains("already registered"));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withDuplicateType_throwsIllegalArgumentException() {
    class SubType {}

    factory.registerSubtype(SubType.class, "label");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> factory.registerSubtype(SubType.class, "anotherLabel"));
    assertTrue(thrown.getMessage().toLowerCase().contains("already registered"));
  }
}