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
import java.lang.reflect.Type;
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

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndClass_returnsCorrectObject() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;
    String expectedObject = "test";

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class);
         MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenMockedStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      @SuppressWarnings("unchecked")
      Gson gsonSpy = spy(gson);

      doReturn(expectedObject).when(gsonSpy).fromJson(jsonElement, typeToken);

      Class<String> wrappedClass = String.class;
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(wrappedClass);

      // Act
      Object result = gsonSpy.fromJson(jsonElement, classOfT);

      // Assert
      assertNotNull(result);
      assertEquals(expectedObject, result);
      assertSame(wrappedClass, Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJsonElement_returnsNull() {
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class);
         MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenMockedStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      @SuppressWarnings("unchecked")
      Gson gsonSpy = spy(gson);

      doReturn(null).when(gsonSpy).fromJson(jsonElement, typeToken);

      Class<String> wrappedClass = String.class;
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(wrappedClass);

      Object result = gsonSpy.fromJson(jsonElement, classOfT);

      assertNull(result);
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_whenFromJsonThrowsJsonSyntaxException_propagatesException() {
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class);
         MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenMockedStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      Class<String> wrappedClass = String.class;
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(wrappedClass);

      @SuppressWarnings("unchecked")
      Gson gsonSpy = spy(gson);

      JsonSyntaxException exception = new JsonSyntaxException("syntax error");
      doThrow(exception).when(gsonSpy).fromJson(jsonElement, typeToken);

      JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class,
          () -> gsonSpy.fromJson(jsonElement, classOfT));
      assertEquals("syntax error", thrown.getMessage());
    }
  }
}