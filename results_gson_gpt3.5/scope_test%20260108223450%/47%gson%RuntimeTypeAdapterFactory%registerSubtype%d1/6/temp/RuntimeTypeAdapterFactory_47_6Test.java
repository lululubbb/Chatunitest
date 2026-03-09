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
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_47_6Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  public void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_NullType_ThrowsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> factory.registerSubtype(null, "label"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_NullLabel_ThrowsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> factory.registerSubtype(Object.class, null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_DuplicateType_ThrowsIllegalArgumentException() throws Exception {
    factory.registerSubtype(Object.class, "label1");

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> factory.registerSubtype(Object.class, "label2"));
    assertEquals("types and labels must be unique", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_DuplicateLabel_ThrowsIllegalArgumentException() throws Exception {
    factory.registerSubtype(Object.class, "label1");

    class SubType extends Object {}

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> factory.registerSubtype(SubType.class, "label1"));
    assertEquals("types and labels must be unique", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void registerSubtype_SuccessfulRegistration_ReturnsThisAndAddsMappings() throws Exception {
    class SubType extends Object {}

    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(SubType.class, "subtypeLabel");
    assertSame(factory, returned);

    // Use reflection to get private fields
    Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    labelToSubtypeField.setAccessible(true);
    subtypeToLabelField.setAccessible(true);

    @SuppressWarnings("unchecked")
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
    @SuppressWarnings("unchecked")
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);

    assertTrue(labelToSubtype.containsKey("subtypeLabel"));
    assertEquals(SubType.class, labelToSubtype.get("subtypeLabel"));

    assertTrue(subtypeToLabel.containsKey(SubType.class));
    assertEquals("subtypeLabel", subtypeToLabel.get(SubType.class));
  }
}