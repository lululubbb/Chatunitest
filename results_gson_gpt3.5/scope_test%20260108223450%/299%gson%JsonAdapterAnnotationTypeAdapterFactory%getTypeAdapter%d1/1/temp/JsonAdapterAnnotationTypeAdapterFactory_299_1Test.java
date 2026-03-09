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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class JsonAdapterAnnotationTypeAdapterFactory_299_1Test {

  @Mock
  ConstructorConstructor constructorConstructorMock;

  @Mock
  Gson gsonMock;

  @Mock
  TypeToken<?> typeTokenMock;

  @Mock
  JsonAdapter jsonAdapterMock;

  @Mock
  TypeAdapter<?> typeAdapterMock;

  @Mock
  TypeAdapterFactory typeAdapterFactoryMock;

  @Mock
  JsonSerializer<?> jsonSerializerMock;

  @Mock
  JsonDeserializer<?> jsonDeserializerMock;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  static class TestTypeAdapter extends TypeAdapter<Object> {
    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }

    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }
  }

  static class TestTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return (TypeAdapter<T>) new TestTypeAdapter();
    }
  }

  static class TestSerializer implements JsonSerializer<Object> {
    @Override
    public com.google.gson.JsonElement serialize(Object src, Type typeOfSrc,
        com.google.gson.JsonSerializationContext context) {
      return null;
    }
  }

  static class TestDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(com.google.gson.JsonElement json, Type typeOfT,
        com.google.gson.JsonDeserializationContext context) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceIsTypeAdapter_nullSafeTrue() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) TestTypeAdapter.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(TestTypeAdapter.class)))
        .thenReturn(() -> new TestTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory,
        constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);

    assertNotNull(adapter);
    assertNotSame(typeAdapterMock, adapter);
    // The returned adapter should be nullSafe so it should be wrapped (TreeTypeAdapter.nullSafe returns this)
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceIsTypeAdapter_nullSafeFalse() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) TestTypeAdapter.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(false);
    when(constructorConstructorMock.get(TypeToken.get(TestTypeAdapter.class)))
        .thenReturn(() -> new TestTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory,
        constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);

    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceIsTypeAdapterFactory() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) TestTypeAdapterFactory.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(TestTypeAdapterFactory.class)))
        .thenReturn(() -> new TestTypeAdapterFactory());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory,
        constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);

    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceIsJsonSerializer() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) TestSerializer.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(TestSerializer.class)))
        .thenReturn(() -> new TestSerializer());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory,
        constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);

    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceIsJsonDeserializer() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) TestDeserializer.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(TestDeserializer.class)))
        .thenReturn(() -> new TestDeserializer());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory,
        constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);

    assertNotNull(adapter);
  }

  static class InvalidInstance {
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_instanceInvalid_throwsException() throws Exception {
    when(jsonAdapterMock.value()).thenReturn((Class) InvalidInstance.class);
    when(jsonAdapterMock.nullSafe()).thenReturn(true);
    when(constructorConstructorMock.get(TypeToken.get(InvalidInstance.class)))
        .thenReturn(() -> new InvalidInstance());

    JsonAdapterAnnotationTypeAdapterFactory factory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(factory, constructorConstructorMock, gsonMock, typeTokenMock, jsonAdapterMock);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertTrue(cause.getMessage().contains("Invalid attempt to bind an instance of"));
  }
}