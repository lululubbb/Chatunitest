package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JsonAdapterAnnotationTypeAdapterFactory_299_5Test {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken<Object> typeTokenMock;
  private JsonAdapter annotationMock;

  @BeforeEach
  public void setUp() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    gsonMock = mock(Gson.class);
    typeTokenMock = TypeToken.get(Object.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);
    annotationMock = mock(JsonAdapter.class);
  }

  private TypeAdapter<?> invokeGetTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson,
                                               TypeToken<?> type, JsonAdapter annotation) throws Exception {
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    return (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, type, annotation);
  }

  static class TestTypeAdapter extends TypeAdapter<Object> {
    @Override
    public void write(JsonWriter out, Object value) {
    }

    @Override
    public Object read(JsonReader in) {
      return null;
    }
  }

  static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return (TypeAdapter<T>) new TestTypeAdapter();
    }
  }

  static class TestJsonSerializer implements JsonSerializer<Object> {
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
      return JsonNull.INSTANCE;
    }
  }

  static class TestJsonDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withTypeAdapterInstance_nullSafeTrue() throws Exception {
    TestTypeAdapter adapterInstance = new TestTypeAdapter();
    when(annotationMock.value()).thenReturn((Class<?>) adapterInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(true);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) adapterInstance.getClass())))
        .thenReturn(() -> (Object) adapterInstance);

    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);

    assertNotNull(result);
    assertNotSame(adapterInstance, result);
    // The returned adapter should be nullSafe wrapped
    assertTrue(result.toString().contains("NullSafe"));
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withTypeAdapterInstance_nullSafeFalse() throws Exception {
    TestTypeAdapter adapterInstance = new TestTypeAdapter();
    when(annotationMock.value()).thenReturn((Class<?>) adapterInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(false);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) adapterInstance.getClass())))
        .thenReturn(() -> (Object) adapterInstance);

    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);

    assertNotNull(result);
    assertSame(adapterInstance, result);
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withTypeAdapterFactoryInstance() throws Exception {
    TestTypeAdapterFactory factoryInstance = new TestTypeAdapterFactory();
    when(annotationMock.value()).thenReturn((Class<?>) factoryInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(true);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) factoryInstance.getClass())))
        .thenReturn(() -> (Object) factoryInstance);

    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);

    assertNotNull(result);
    // Should be nullSafe wrapped
    assertTrue(result.toString().contains("NullSafe"));
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withJsonSerializerInstance() throws Exception {
    TestJsonSerializer serializerInstance = new TestJsonSerializer();
    when(annotationMock.value()).thenReturn((Class<?>) serializerInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(true);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) serializerInstance.getClass())))
        .thenReturn(() -> (Object) serializerInstance);

    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);

    assertNotNull(result);
    // TreeTypeAdapter disables nullSafe in this case, so result should not be nullSafe wrapped
    assertFalse(result.toString().contains("NullSafe"));
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withJsonDeserializerInstance() throws Exception {
    TestJsonDeserializer deserializerInstance = new TestJsonDeserializer();
    when(annotationMock.value()).thenReturn((Class<?>) deserializerInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(true);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) deserializerInstance.getClass())))
        .thenReturn(() -> (Object) deserializerInstance);

    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);

    assertNotNull(result);
    // TreeTypeAdapter disables nullSafe in this case, so result should not be nullSafe wrapped
    assertFalse(result.toString().contains("NullSafe"));
  }

  static class InvalidClass {
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withInvalidInstance_throws() throws Exception {
    InvalidClass invalidInstance = new InvalidClass();
    when(annotationMock.value()).thenReturn((Class<?>) invalidInstance.getClass());
    when(annotationMock.nullSafe()).thenReturn(true);
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    when(constructorConstructor.get(TypeToken.get((Class<?>) invalidInstance.getClass())))
        .thenReturn(() -> (Object) invalidInstance);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      invokeGetTypeAdapter(constructorConstructor, gsonMock, typeTokenMock, annotationMock);
    });

    String expectedMessagePart = InvalidClass.class.getName();
    assertTrue(exception.getMessage().contains(expectedMessagePart));
    assertTrue(exception.getMessage().contains("@JsonAdapter"));
  }
}