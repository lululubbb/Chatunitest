package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

final class JsonAdapterAnnotationTypeAdapterFactory_299_6Test {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor mockConstructorConstructor;
  private Gson mockGson;
  private TypeToken<String> stringTypeToken;

  @BeforeEach
  void setUp() {
    mockConstructorConstructor = mock(ConstructorConstructor.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);
    mockGson = mock(Gson.class);
    stringTypeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withTypeAdapterInstance_nullSafeTrue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockTypeAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
    when(mockTypeAdapter.nullSafe()).thenReturn(mockTypeAdapter);

    JsonAdapter annotation = createJsonAdapterAnnotation(TypeAdapterMock.class, true);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<TypeAdapter<?>> constructor =
        () -> mockTypeAdapter;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation);

    assertSame(mockTypeAdapter, result);
    verify(mockTypeAdapter).nullSafe();
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withTypeAdapterInstance_nullSafeFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockTypeAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
    when(mockTypeAdapter.nullSafe()).thenReturn(mockTypeAdapter);

    JsonAdapter annotation = createJsonAdapterAnnotation(TypeAdapterMock.class, false);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<TypeAdapter<?>> constructor =
        () -> mockTypeAdapter;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation);

    assertSame(mockTypeAdapter, result);
    verify(mockTypeAdapter, never()).nullSafe();
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withTypeAdapterFactoryInstance_nullSafeTrue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> returnedAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapterFactory mockFactory = (TypeAdapterFactory) mock(TypeAdapterFactory.class);
    when(mockFactory.create(mockGson, stringTypeToken)).thenReturn(returnedAdapter);
    when(returnedAdapter.nullSafe()).thenReturn(returnedAdapter);

    JsonAdapter annotation = createJsonAdapterAnnotation(TypeAdapterFactoryMock.class, true);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<TypeAdapterFactory> constructor =
        () -> mockFactory;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation);

    assertSame(returnedAdapter, result);
    verify(mockFactory).create(mockGson, stringTypeToken);
    verify(returnedAdapter).nullSafe();
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withJsonSerializerInstance_nullSafeTrue() throws Exception {
    @SuppressWarnings("unchecked")
    JsonSerializer<String> mockSerializer = (JsonSerializer<String>) mock(JsonSerializer.class);

    JsonAdapter annotation = createJsonAdapterAnnotation(JsonSerializerMock.class, true);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<JsonSerializer<?>> constructor =
        () -> mockSerializer;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation);

    assertNotNull(result);
    assertTrue(TypeAdapter.class.isAssignableFrom(result.getClass()));

    // nullSafe should not be called on TreeTypeAdapter, so result should not be nullSafe wrapped
    // We cannot verify internals of TreeTypeAdapter, but we can check that returned adapter is not nullSafe wrapped
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withJsonDeserializerInstance_nullSafeFalse() throws Exception {
    @SuppressWarnings("unchecked")
    JsonDeserializer<String> mockDeserializer = (JsonDeserializer<String>) mock(JsonDeserializer.class);

    JsonAdapter annotation = createJsonAdapterAnnotation(JsonDeserializerMock.class, false);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<JsonDeserializer<?>> constructor =
        () -> mockDeserializer;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation);

    assertNotNull(result);
    assertTrue(TypeAdapter.class.isAssignableFrom(result.getClass()));
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withInvalidInstance_throwsException() throws Exception {
    Object invalidInstance = new Object();

    JsonAdapter annotation = createJsonAdapterAnnotation(Object.class, true);

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<Object> constructor =
        () -> invalidInstance;
    when(mockConstructorConstructor.get(TypeToken.get(annotation.value())))
        .thenReturn(constructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () ->
        method.invoke(factory, mockConstructorConstructor, mockGson, stringTypeToken, annotation));

    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalArgumentException);
    assertTrue(cause.getMessage().contains("Invalid attempt to bind an instance of"));
  }

  // Helper method to create a JsonAdapter annotation instance for testing
  private JsonAdapter createJsonAdapterAnnotation(Class<?> clazz, boolean nullSafe) {
    return new JsonAdapter() {
      @Override
      public Class<?> value() {
        return clazz;
      }

      @Override
      public boolean nullSafe() {
        return nullSafe;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };
  }

  // Dummy classes to simulate instances returned by ConstructorConstructor.get().construct()
  private static final class TypeAdapterMock extends TypeAdapter<Object> {
    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }

    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }
  }

  private static final class TypeAdapterFactoryMock implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return new TypeAdapterMock();
    }
  }

  private static final class JsonSerializerMock implements JsonSerializer<String> {
    @Override
    public com.google.gson.JsonElement serialize(String src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
      return null;
    }
  }

  private static final class JsonDeserializerMock implements JsonDeserializer<String> {
    @Override
    public String deserialize(com.google.gson.JsonElement json, Type typeOfT,
                              com.google.gson.JsonDeserializationContext context) {
      return null;
    }
  }
}