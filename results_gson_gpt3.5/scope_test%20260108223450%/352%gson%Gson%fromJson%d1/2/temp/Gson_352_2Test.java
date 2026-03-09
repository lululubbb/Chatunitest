package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementAndClass_returnsCorrectInstance() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;

    // Mock static methods of TypeToken
    try (MockedStatic<TypeToken> typeTokenStatic = Mockito.mockStatic(TypeToken.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      String expectedObject = "testString";

      // Spy on gson to mock fromJson(JsonElement, TypeToken<T>)
      Gson spyGson = Mockito.spy(gson);
      doReturn(expectedObject).when(spyGson).fromJson(jsonElement, typeToken);

      // Act
      String result = spyGson.fromJson(jsonElement, classOfT);

      // Assert
      assertEquals(expectedObject, result);
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJsonElement_returnsNull() {
    // Arrange
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    try (MockedStatic<TypeToken> typeTokenStatic = Mockito.mockStatic(TypeToken.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      Gson spyGson = Mockito.spy(gson);
      doReturn(null).when(spyGson).fromJson(jsonElement, typeToken);

      // Act
      String result = spyGson.fromJson(jsonElement, classOfT);

      // Assert
      assertNull(result);
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withPrimitiveClass_returnsCorrectInstance() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<Integer> classOfT = Integer.class;

    try (MockedStatic<TypeToken> typeTokenStatic = Mockito.mockStatic(TypeToken.class)) {

      TypeToken<Integer> typeToken = TypeToken.get(classOfT);
      typeTokenStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      Integer expectedObject = 123;

      Gson spyGson = Mockito.spy(gson);
      doReturn(expectedObject).when(spyGson).fromJson(jsonElement, typeToken);

      // Act
      Integer result = spyGson.fromJson(jsonElement, classOfT);

      // Assert
      assertEquals(expectedObject, result);
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }
}