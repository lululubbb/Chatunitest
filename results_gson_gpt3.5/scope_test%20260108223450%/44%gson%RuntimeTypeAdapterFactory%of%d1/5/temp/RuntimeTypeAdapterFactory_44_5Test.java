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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class RuntimeTypeAdapterFactory_44_5Test {

  @Test
    @Timeout(8000)
  public void testOf_withBaseTypeAndTypeFieldName() throws Exception {
    // Invoke the static method RuntimeTypeAdapterFactory.of(Class<T>, String)
    Class<?> clazz = RuntimeTypeAdapterFactory.class;
    Method ofMethod = clazz.getDeclaredMethod("of", Class.class, String.class);
    @SuppressWarnings("unchecked")
    RuntimeTypeAdapterFactory<Object> factory = (RuntimeTypeAdapterFactory<Object>) ofMethod.invoke(null, Object.class, "type");

    assertNotNull(factory);
    // Check private fields via reflection
    var baseTypeField = clazz.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    assertEquals(Object.class, baseTypeField.get(factory));

    var typeFieldNameField = clazz.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    assertEquals("type", typeFieldNameField.get(factory));

    var maintainTypeField = clazz.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    assertFalse(maintainTypeField.getBoolean(factory));
  }

  @Test
    @Timeout(8000)
  public void testOf_withBaseTypeAndTypeFieldNameAndMaintainType() throws Exception {
    // Invoke the static method RuntimeTypeAdapterFactory.of(Class<T>, String, boolean)
    Class<?> clazz = RuntimeTypeAdapterFactory.class;
    Method ofMethod = clazz.getDeclaredMethod("of", Class.class, String.class, boolean.class);
    @SuppressWarnings("unchecked")
    RuntimeTypeAdapterFactory<Object> factory = (RuntimeTypeAdapterFactory<Object>) ofMethod.invoke(null, Object.class, "type", true);

    assertNotNull(factory);
    var baseTypeField = clazz.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    assertEquals(Object.class, baseTypeField.get(factory));

    var typeFieldNameField = clazz.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    assertEquals("type", typeFieldNameField.get(factory));

    var maintainTypeField = clazz.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    assertTrue(maintainTypeField.getBoolean(factory));
  }

  @Test
    @Timeout(8000)
  public void testOf_withOnlyBaseType() throws Exception {
    // Invoke the static method RuntimeTypeAdapterFactory.of(Class<T>)
    Class<?> clazz = RuntimeTypeAdapterFactory.class;
    Method ofMethod = clazz.getDeclaredMethod("of", Class.class);
    @SuppressWarnings("unchecked")
    RuntimeTypeAdapterFactory<Object> factory = (RuntimeTypeAdapterFactory<Object>) ofMethod.invoke(null, Object.class);

    assertNotNull(factory);
    var baseTypeField = clazz.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    assertEquals(Object.class, baseTypeField.get(factory));

    var typeFieldNameField = clazz.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    assertEquals("type", typeFieldNameField.get(factory)); // Default typeFieldName is "type"

    var maintainTypeField = clazz.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    assertFalse(maintainTypeField.getBoolean(factory));
  }
}