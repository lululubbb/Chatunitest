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
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JsonAdapterAnnotationTypeAdapterFactory_299_4Test {

  private ConstructorConstructor constructorConstructorMock;
  private Gson gsonMock;
  private TypeToken<String> typeToken;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    gsonMock = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructorMock);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withTypeAdapterInstance_returnsNullSafeAdapter() throws Exception {
    // Arrange
    TypeAdapter<String> typeAdapterInstance = mock(TypeAdapter.class);
    Class<?> typeAdapterClass = typeAdapterInstance.getClass();

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return typeAdapterClass;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> constructor = () -> typeAdapterInstance;
    when(constructorConstructorMock.get(TypeToken.get(typeAdapterClass))).thenReturn((ObjectConstructor) constructor);

    // Act
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
    verify(typeAdapterInstance, never()).nullSafe(); // nullSafe() is a method to wrap adapter, not called on adapter itself
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withTypeAdapterFactoryInstance_returnsCreatedAdapter() throws Exception {
    // Arrange
    TypeAdapterFactory factoryInstance = mock(TypeAdapterFactory.class);
    Class<?> factoryClass = factoryInstance.getClass();
    TypeAdapter<String> createdAdapter = mock(TypeAdapter.class);
    when(factoryInstance.create(gsonMock, typeToken)).thenReturn(createdAdapter);
    @SuppressWarnings("unchecked")
    ObjectConstructor<?> constructor = () -> factoryInstance;
    when(constructorConstructorMock.get(TypeToken.get(factoryClass))).thenReturn((ObjectConstructor) constructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return factoryClass;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };

    // Act
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
    assertSame(createdAdapter, result);
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withJsonSerializerInstance_returnsTreeTypeAdapter() throws Exception {
    // Arrange
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    Class<?> serializerClass = serializer.getClass();

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> constructor = () -> serializer;
    when(constructorConstructorMock.get(TypeToken.get(serializerClass))).thenReturn((ObjectConstructor) constructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return serializerClass;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };

    // Act
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter", result.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withJsonDeserializerInstance_returnsTreeTypeAdapter() throws Exception {
    // Arrange
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Class<?> deserializerClass = deserializer.getClass();

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> constructor = () -> deserializer;
    when(constructorConstructorMock.get(TypeToken.get(deserializerClass))).thenReturn((ObjectConstructor) constructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return deserializerClass;
      }

      @Override
      public boolean nullSafe() {
        return false;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };

    // Act
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);
    TypeAdapter<?> result = (TypeAdapter<?>) method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, annotation);

    // Assert
    assertNotNull(result);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter", result.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testGetTypeAdapter_withInvalidInstance_throwsIllegalArgumentException() throws Exception {
    // Arrange
    Object invalidInstance = new Object();
    Class<?> invalidClass = invalidInstance.getClass();

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> constructor = () -> invalidInstance;
    when(constructorConstructorMock.get(TypeToken.get(invalidClass))).thenReturn((ObjectConstructor) constructor);

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<?> value() {
        return invalidClass;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return JsonAdapter.class;
      }
    };

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    // Act & Assert
    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(factory, constructorConstructorMock, gsonMock, typeToken, annotation);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalArgumentException);
    assertTrue(cause.getMessage().contains("Invalid attempt to bind an instance of"));
  }
}