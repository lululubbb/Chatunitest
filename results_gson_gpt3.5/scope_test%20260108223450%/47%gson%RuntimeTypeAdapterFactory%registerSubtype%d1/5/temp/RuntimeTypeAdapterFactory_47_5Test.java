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

class RuntimeTypeAdapterFactory_47_5Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "typeField", false);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_nullType_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(null, "label");
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_nullLabel_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      factory.registerSubtype(Object.class, null);
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateType_throwsIllegalArgumentException() {
    factory.registerSubtype(Object.class, "label1");
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Object.class, "label2");
    });
    assertEquals("types and labels must be unique", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateLabel_throwsIllegalArgumentException() {
    factory.registerSubtype(String.class, "label1");
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      factory.registerSubtype(Integer.class, "label1");
    });
    assertEquals("types and labels must be unique", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerSubtype_validSubtype_returnsSameFactory() {
    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(String.class, "stringLabel");
    assertSame(factory, returned);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_addsEntriesToMaps() throws Exception {
    factory.registerSubtype(String.class, "stringLabel");

    // Use reflection to access private fields
    var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);

    var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);

    assertTrue(labelToSubtype.containsKey("stringLabel"));
    assertEquals(String.class, labelToSubtype.get("stringLabel"));
    assertTrue(subtypeToLabel.containsKey(String.class));
    assertEquals("stringLabel", subtypeToLabel.get(String.class));
  }
}