package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_42_6Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  public void setUp() throws Exception {
    Constructor<RuntimeTypeAdapterFactory> constructor =
        RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    factory = constructor.newInstance(Object.class, "type", false);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_nullArguments_throwsNullPointerException() throws Exception {
    Constructor<RuntimeTypeAdapterFactory> constructor =
        RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);

    // The constructor will wrap the NullPointerException in InvocationTargetException,
    // so we need to unwrap and check the cause.
    assertThrows(
        NullPointerException.class,
        () -> {
          try {
            constructor.newInstance(null, "type", false);
          } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof NullPointerException) {
              throw (NullPointerException) cause;
            }
            throw e;
          }
        });
    assertThrows(
        NullPointerException.class,
        () -> {
          try {
            constructor.newInstance(Object.class, null, false);
          } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof NullPointerException) {
              throw (NullPointerException) cause;
            }
            throw e;
          }
        });
  }

  @Test
    @Timeout(8000)
  public void testOf_methods_createInstance() {
    RuntimeTypeAdapterFactory<Object> f1 = RuntimeTypeAdapterFactory.of(Object.class, "type", true);
    assertNotNull(f1);
    RuntimeTypeAdapterFactory<Object> f2 = RuntimeTypeAdapterFactory.of(Object.class, "type");
    assertNotNull(f2);
    RuntimeTypeAdapterFactory<Object> f3 = RuntimeTypeAdapterFactory.of(Object.class);
    assertNotNull(f3);
  }

  @Test
    @Timeout(8000)
  public void testRecognizeSubtypes_returnsThis() {
    RuntimeTypeAdapterFactory<Object> result = factory.recognizeSubtypes();
    assertSame(factory, result);
  }

  @Test
    @Timeout(8000)
  public void testRegisterSubtype_withLabel_and_withoutLabel() {
    RuntimeTypeAdapterFactory<Object> returned1 = factory.registerSubtype(String.class, "string");
    assertSame(factory, returned1);
    RuntimeTypeAdapterFactory<Object> returned2 = factory.registerSubtype(Integer.class);
    assertSame(factory, returned2);

    // Check internal maps via reflection
    Map<String, Class<?>> labelToSubtype = getPrivateField(factory, "labelToSubtype");
    Map<Class<?>, String> subtypeToLabel = getPrivateField(factory, "subtypeToLabel");

    assertEquals(String.class, labelToSubtype.get("string"));
    assertEquals("string", subtypeToLabel.get(String.class));
    assertTrue(labelToSubtype.containsValue(Integer.class));
    assertTrue(subtypeToLabel.containsKey(Integer.class));
  }

  @Test
    @Timeout(8000)
  public void testCreate_nullType_returnsNull() {
    Gson gson = mock(Gson.class);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    // Create a RuntimeTypeAdapterFactory for String.class to test create returns null for non-matching type
    RuntimeTypeAdapterFactory<String> stringFactory = RuntimeTypeAdapterFactory.of(String.class);
    TypeAdapter<?> adapter = stringFactory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withRegisteredSubtype() throws IOException, NoSuchFieldException, IllegalAccessException {
    Gson gson = mock(Gson.class);
    TypeToken<Object> baseTypeToken = TypeToken.get(Object.class);

    factory.registerSubtype(String.class, "string");
    factory.registerSubtype(Integer.class);

    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    TypeAdapter<Integer> integerAdapter = mock(TypeAdapter.class);

    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(stringAdapter);
    when(gson.getAdapter(TypeToken.get(Integer.class))).thenReturn(integerAdapter);

    TypeAdapter<Object> adapter = factory.create(gson, baseTypeToken);
    assertNotNull(adapter);

    // Mock JsonWriter for writing
    JsonWriter jsonWriter = mock(JsonWriter.class);

    // Access private field 'typeFieldName' via reflection
    Field typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    String typeFieldName = (String) typeFieldNameField.get(factory);

    // Write test
    Object value = "testString";

    // We must mock stringAdapter.write() to avoid exception during adapter.write()
    doNothing().when(stringAdapter).write(any(JsonWriter.class), eq("testString"));

    adapter.write(jsonWriter, value);

    verify(jsonWriter).beginObject();
    verify(jsonWriter).name(typeFieldName);
    verify(jsonWriter).value("string");
    verify(stringAdapter).write(any(JsonWriter.class), eq("testString"));
    verify(jsonWriter).endObject();

    // Read test - simulate JsonElement with type field "string"
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(typeFieldName, "string");
    JsonElement jsonElement = jsonObject;

    // Mock gson.fromJson to return "testString" when called with jsonElement and String.class
    when(gson.fromJson(jsonElement, String.class)).thenReturn("testString");

    // Because read uses JsonReader which is complex to mock fully, we skip read coverage here.
    // Full coverage would require integration test or complex mocks.
  }

  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object instance, String fieldName) {
    try {
      java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}