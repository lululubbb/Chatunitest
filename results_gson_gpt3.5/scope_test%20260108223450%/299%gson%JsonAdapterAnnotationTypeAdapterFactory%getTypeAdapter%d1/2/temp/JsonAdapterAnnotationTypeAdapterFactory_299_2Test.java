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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonAdapterAnnotationTypeAdapterFactory_299_2Test {

  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken<Object> typeToken;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    gsonMock = mock(Gson.class);
    typeToken = TypeToken.get(Object.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceIsTypeAdapter_returnsTypeAdapterNullSafe() throws Exception {
    // Arrange
    @JsonAdapter(TestTypeAdapter.class)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> instance = mock(TypeAdapter.class);
    TypeAdapter<?> nullSafeAdapter = mock(TypeAdapter.class);

    // Use raw ObjectConstructor class via reflection to mock
    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    Method constructMethod = objectConstructorClass.getMethod("construct");
    when(constructMethod.invoke(objectConstructorMock)).thenReturn(instance); // won't work, use when on mock directly below

    // Instead of invoking constructMethod.invoke(objectConstructorMock), use Mockito to stub construct()
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(instance);

    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    when(instance.nullSafe()).thenReturn(nullSafeAdapter);

    // Act
    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertSame(nullSafeAdapter, result);
    verify(instance).nullSafe();
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceIsTypeAdapter_nullSafeFalse_returnsTypeAdapter() throws Exception {
    // Arrange
    @JsonAdapter(value = TestTypeAdapter.class, nullSafe = false)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> instance = mock(TypeAdapter.class);

    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(instance);
    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    // Act
    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertSame(instance, result);
    verify(instance, never()).nullSafe();
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceIsTypeAdapterFactory_returnsCreatedTypeAdapter() throws Exception {
    // Arrange
    @JsonAdapter(TestTypeAdapterFactory.class)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    TypeAdapter<?> createdAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory instance = mock(TypeAdapterFactory.class);

    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(instance);
    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    when(instance.create(gsonMock, typeToken)).thenReturn(createdAdapter);
    when(createdAdapter.nullSafe()).thenReturn(createdAdapter);

    // Act
    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertSame(createdAdapter, result);
    verify(instance).create(gsonMock, typeToken);
    verify(createdAdapter).nullSafe();
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceIsJsonSerializer_returnsTreeTypeAdapter() throws Exception {
    // Arrange
    @JsonAdapter(TestJsonSerializer.class)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    JsonSerializer<?> serializer = mock(JsonSerializer.class);

    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(serializer);
    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    // Act
    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
    assertNotSame(serializer, result); // result is TreeTypeAdapter
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceIsJsonDeserializer_returnsTreeTypeAdapter() throws Exception {
    // Arrange
    @JsonAdapter(TestJsonDeserializer.class)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(deserializer);
    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    // Act
    TypeAdapter<?> result = invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_instanceInvalid_throwsIllegalArgumentException() throws Exception {
    // Arrange
    @JsonAdapter(TestInvalid.class)
    class Dummy {
    }

    JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

    Object invalidInstance = new Object();

    @SuppressWarnings("unchecked")
    Class<?> objectConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor$ObjectConstructor");
    Object objectConstructorMock = mock(objectConstructorClass);
    when(((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock).construct()).thenReturn(invalidInstance);
    when(constructorConstructorMock.get(TypeToken.get(annotation.value()))).thenReturn((ConstructorConstructor.ObjectConstructor<?>) objectConstructorMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeGetTypeAdapter(constructorConstructorMock, gsonMock, typeToken, annotation);
    });
    assertTrue(thrown.getMessage().contains("Invalid attempt to bind an instance of"));
  }

  private TypeAdapter<?> invokeGetTypeAdapter(ConstructorConstructor cc, Gson gson, TypeToken<?> type, JsonAdapter annotation) throws Exception {
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    return (TypeAdapter<?>) method.invoke(factory, cc, gson, type, annotation);
  }

  // Dummy classes for annotations
  @JsonAdapter(TestTypeAdapter.class)
  static class TestTypeAdapter extends TypeAdapter<Object> {
    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }
  }

  @JsonAdapter(value = TestTypeAdapter.class, nullSafe = false)
  static class TestTypeAdapterNoNullSafe extends TypeAdapter<Object> {
    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }
  }

  @JsonAdapter(TestTypeAdapterFactory.class)
  static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return mock(TypeAdapter.class);
    }
  }

  @JsonAdapter(TestJsonSerializer.class)
  static class TestJsonSerializer implements JsonSerializer<Object> {
    @Override
    public com.google.gson.JsonElement serialize(Object src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
      return null;
    }
  }

  @JsonAdapter(TestJsonDeserializer.class)
  static class TestJsonDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) {
      return null;
    }
  }

  @JsonAdapter(TestInvalid.class)
  static class TestInvalid {
  }
}