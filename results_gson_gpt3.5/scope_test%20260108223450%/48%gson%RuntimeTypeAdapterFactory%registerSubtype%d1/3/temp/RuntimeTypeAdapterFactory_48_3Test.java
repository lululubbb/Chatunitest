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
import java.lang.reflect.Method;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

class RuntimeTypeAdapterFactory_48_3Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type");
  }

  @Test
    @Timeout(8000)
  void testRegisterSubtype_withClassOnly() {
    class Subtype {}

    RuntimeTypeAdapterFactory<Object> returned = factory.registerSubtype(Subtype.class);

    assertSame(factory, returned);

    try {
      // Access private field subtypeToLabel via reflection to verify label mapping
      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var map = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertTrue(map.containsKey(Subtype.class));
      assertEquals(Subtype.class.getSimpleName(), map.get(Subtype.class));

      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var map2 = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertTrue(map2.containsKey(Subtype.class.getSimpleName()));
      assertEquals(Subtype.class, map2.get(Subtype.class.getSimpleName()));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testRegisterSubtype_withDuplicateLabel_throws() {
    class Subtype1 {}
    class Subtype2 {}

    factory.registerSubtype(Subtype1.class);

    RuntimeTypeAdapterFactory<Object> factorySpy = factory;

    // Use reflection to invoke private registerSubtype(type,label) to register duplicate label
    try {
      Method registerSubtypeWithLabel = RuntimeTypeAdapterFactory.class.getDeclaredMethod("registerSubtype", Class.class, String.class);
      registerSubtypeWithLabel.setAccessible(true);

      // This should throw IllegalArgumentException because label already exists
      IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        try {
          registerSubtypeWithLabel.invoke(factorySpy, Subtype2.class, Subtype1.class.getSimpleName());
        } catch (Exception e) {
          // unwrap InvocationTargetException
          Throwable cause = e.getCause();
          if (cause instanceof IllegalArgumentException) {
            throw (IllegalArgumentException) cause;
          }
          throw e;
        }
      });
      assertEquals("types and labels must be unique", thrown.getMessage());
    } catch (NoSuchMethodException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }
}