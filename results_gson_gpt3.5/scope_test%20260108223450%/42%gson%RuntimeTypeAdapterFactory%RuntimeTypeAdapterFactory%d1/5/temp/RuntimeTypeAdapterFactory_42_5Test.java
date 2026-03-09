package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_42_5Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  void setup() throws Exception {
    // Use reflection to invoke private constructor
    Class<?> clazz = Class.forName("com.google.gson.typeadapters.RuntimeTypeAdapterFactory");
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(Object.class, "type", false);
  }

  @Test
    @Timeout(8000)
  void constructor_nullBaseType_throws() throws Exception {
    Class<?> clazz = Class.forName("com.google.gson.typeadapters.RuntimeTypeAdapterFactory");
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    Throwable thrown = assertThrows(Throwable.class, () -> constructor.newInstance(null, "type", false));
    // unwrap InvocationTargetException to check cause
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void constructor_nullTypeFieldName_throws() throws Exception {
    Class<?> clazz = Class.forName("com.google.gson.typeadapters.RuntimeTypeAdapterFactory");
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    Throwable thrown = assertThrows(Throwable.class, () -> constructor.newInstance(Object.class, null, false));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void of_withAllParameters_createsInstance() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", true);
    assertNotNull(created);
    // Check fields via reflection
    assertEquals(Object.class, getField(created, "baseType"));
    assertEquals("type", getField(created, "typeFieldName"));
    assertEquals(true, getField(created, "maintainType"));
  }

  @Test
    @Timeout(8000)
  void of_withTypeFieldName_createsInstance() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type");
    assertNotNull(created);
    assertEquals(Object.class, getField(created, "baseType"));
    assertEquals("type", getField(created, "typeFieldName"));
    assertEquals(false, getField(created, "maintainType"));
  }

  @Test
    @Timeout(8000)
  void of_withBaseType_createsInstance() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class);
    assertNotNull(created);
    assertEquals(Object.class, getField(created, "baseType"));
    assertEquals("type", getField(created, "typeFieldName"));
    assertEquals(false, getField(created, "maintainType"));
  }

  @Test
    @Timeout(8000)
  void recognizeSubtypes_setsFlag() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    RuntimeTypeAdapterFactory<Object> returned = created.recognizeSubtypes();
    assertSame(created, returned);
    assertTrue((Boolean) getField(created, "recognizeSubtypes"));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withLabel_addsMapping() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    class Subtype {}
    RuntimeTypeAdapterFactory<Object> returned = created.registerSubtype(Subtype.class, "label");
    assertSame(created, returned);
    Map<String, Class<?>> labelToSubtype = getField(created, "labelToSubtype");
    Map<Class<?>, String> subtypeToLabel = getField(created, "subtypeToLabel");
    assertEquals(Subtype.class, labelToSubtype.get("label"));
    assertEquals("label", subtypeToLabel.get(Subtype.class));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withoutLabel_addsMapping() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    class Subtype {}
    RuntimeTypeAdapterFactory<Object> returned = created.registerSubtype(Subtype.class);
    assertSame(created, returned);
    Map<String, Class<?>> labelToSubtype = getField(created, "labelToSubtype");
    Map<Class<?>, String> subtypeToLabel = getField(created, "subtypeToLabel");
    String label = subtypeToLabel.get(Subtype.class);
    assertNotNull(label);
    assertEquals(Subtype.class, labelToSubtype.get(label));
  }

  @Test
    @Timeout(8000)
  void create_returnsTypeAdapter_withSubtype() throws IOException {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    class Subtype {}
    created.registerSubtype(Subtype.class, "subtypeLabel");
    Gson gson = new Gson();
    TypeToken<Subtype> typeToken = TypeToken.get(Subtype.class);
    TypeAdapter<Subtype> adapter = created.create(gson, typeToken);
    assertNotNull(adapter);
    // The adapter should be able to write and read JSON with subtype label
    JsonObject json = new JsonObject();
    json.addProperty("field", "value");
    JsonWriter writer = mock(JsonWriter.class);
    JsonReader reader = mock(JsonReader.class);
    // We test that adapter is not null and can be used, deeper tests require Gson internals
  }

  @Test
    @Timeout(8000)
  void create_returnsNull_forUnregisteredSubtype() {
    RuntimeTypeAdapterFactory<Object> created = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    Gson gson = new Gson();
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = created.create(gson, typeToken);
    assertNull(adapter);
  }

  // Helper method to get private fields
  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) {
    try {
      Class<?> clazz = instance.getClass();
      while (clazz != null) {
        try {
          Field field = clazz.getDeclaredField(fieldName);
          field.setAccessible(true);
          return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
          clazz = clazz.getSuperclass();
        }
      }
      throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}