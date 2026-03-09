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
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonAdapterAnnotationTypeAdapterFactory_299_3Test {

  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken<String> typeToken;
  private JsonAdapter jsonAdapterMock;

  @BeforeEach
  public void setUp() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    gsonMock = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);
    typeToken = TypeToken.get(String.class);
    jsonAdapterMock = mock(JsonAdapter.class);
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withTypeAdapterInstance() throws Exception {
    TypeAdapter<?> typeAdapterMock = mock(TypeAdapter.class);
    when(jsonAdapterMock.value()).thenReturn((Class) typeAdapterMock.getClass());
    when(jsonAdapterMock.nullSafe()).thenReturn(true);

    Object instance = typeAdapterMock;
    when(constructorConstructorMock.get(TypeToken.get(jsonAdapterMock.value()))).thenReturn(() -> instance);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, jsonAdapterMock);

    verify(typeAdapterMock).nullSafe();
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withTypeAdapterFactoryInstance() throws Exception {
    TypeAdapter<?> typeAdapterMock = mock(TypeAdapter.class);
    TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);
    when(jsonAdapterMock.value()).thenReturn((Class) typeAdapterFactoryMock.getClass());
    when(jsonAdapterMock.nullSafe()).thenReturn(true);

    when(constructorConstructorMock.get(TypeToken.get(jsonAdapterMock.value()))).thenReturn(() -> typeAdapterFactoryMock);
    when(typeAdapterFactoryMock.create(gsonMock, typeToken)).thenReturn(typeAdapterMock);
    when(typeAdapterMock.nullSafe()).thenReturn(typeAdapterMock);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, jsonAdapterMock);

    verify(typeAdapterFactoryMock).create(gsonMock, typeToken);
    verify(typeAdapterMock).nullSafe();
    assertNotNull(result);
  }

  static class TestJsonSerializer implements JsonSerializer<String> {
    @Override
    public com.google.gson.JsonElement serialize(String src, java.lang.reflect.Type typeOfSrc,
        com.google.gson.JsonSerializationContext context) {
      return null;
    }
  }

  static class TestJsonDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
        com.google.gson.JsonDeserializationContext context) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withJsonSerializerInstance() throws Exception {
    TestJsonSerializer serializer = new TestJsonSerializer();
    when(jsonAdapterMock.value()).thenReturn((Class) serializer.getClass());
    when(jsonAdapterMock.nullSafe()).thenReturn(true);

    when(constructorConstructorMock.get(TypeToken.get(jsonAdapterMock.value()))).thenReturn(() -> serializer);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, jsonAdapterMock);

    assertNotNull(result);
    // TreeTypeAdapter disables nullSafe in this case, so result.nullSafe() is not called
  }

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withJsonDeserializerInstance() throws Exception {
    TestJsonDeserializer deserializer = new TestJsonDeserializer();
    when(jsonAdapterMock.value()).thenReturn((Class) deserializer.getClass());
    when(jsonAdapterMock.nullSafe()).thenReturn(true);

    when(constructorConstructorMock.get(TypeToken.get(jsonAdapterMock.value()))).thenReturn(() -> deserializer);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, jsonAdapterMock);

    assertNotNull(result);
  }

  static class InvalidClass {}

  @Test
    @Timeout(8000)
  public void testGetTypeAdapter_withInvalidInstance_throws() throws Exception {
    InvalidClass invalidInstance = new InvalidClass();
    when(jsonAdapterMock.value()).thenReturn((Class) invalidInstance.getClass());
    when(jsonAdapterMock.nullSafe()).thenReturn(true);

    when(constructorConstructorMock.get(TypeToken.get(jsonAdapterMock.value()))).thenReturn(() -> invalidInstance);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, jsonAdapterMock);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    assertTrue(thrown.getCause().getMessage().contains("Invalid attempt to bind an instance of"));
  }
}