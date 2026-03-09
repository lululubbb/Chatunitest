package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class Gson_fromJson_JsonElement_Class_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonElementAndClass_returnsCastedObject() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;
    String expectedObject = "testValue";

    // Mock static methods Primitives.wrap and cast
    try (MockedStatic<Primitives> primitivesMockedStatic = mockStatic(Primitives.class)) {
      // Mock Primitives.wrap(classOfT) to return Class<String>
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      // Spy on gson to mock fromJson(JsonElement, TypeToken)
      Gson spyGson = spy(gson);
      TypeToken<String> typeToken = TypeToken.get(classOfT);

      doReturn(expectedObject).when(spyGson).fromJson(jsonElement, typeToken);

      // Act
      Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Class.class);
      fromJsonMethod.setAccessible(true);
      @SuppressWarnings("unchecked")
      String actualObject = (String) fromJsonMethod.invoke(spyGson, jsonElement, classOfT);

      // Assert
      assertEquals(expectedObject, actualObject);
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJsonElement_returnsNull() throws Exception {
    // Arrange
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    try (MockedStatic<Primitives> primitivesMockedStatic = mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      Gson spyGson = spy(gson);
      TypeToken<String> typeToken = TypeToken.get(classOfT);

      doReturn(null).when(spyGson).fromJson(jsonElement, typeToken);

      // Act
      Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Class.class);
      fromJsonMethod.setAccessible(true);
      @SuppressWarnings("unchecked")
      String actualObject = (String) fromJsonMethod.invoke(spyGson, jsonElement, classOfT);

      // Assert
      assertNull(actualObject);
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_whenFromJsonThrowsJsonSyntaxException_propagatesException() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;

    try (MockedStatic<Primitives> primitivesMockedStatic = mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      Gson spyGson = spy(gson);
      TypeToken<String> typeToken = TypeToken.get(classOfT);

      doThrow(new JsonSyntaxException("syntax error")).when(spyGson).fromJson(jsonElement, typeToken);

      // Act & Assert
      Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Class.class);
      fromJsonMethod.setAccessible(true);

      InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
        fromJsonMethod.invoke(spyGson, jsonElement, classOfT);
      });
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof JsonSyntaxException);
      assertEquals("syntax error", cause.getMessage());

      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }
}