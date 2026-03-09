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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndType_shouldDelegateToFromJsonWithTypeToken() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Type type = String.class;
    @SuppressWarnings("rawtypes")
    TypeToken expectedTypeToken = TypeToken.get(type);

    // Spy on gson to verify delegation
    Gson spyGson = spy(gson);

    // Mock the fromJson(JsonElement, TypeToken) to return a specific value
    String expectedResult = "testResult";

    // Use reflection to get the exact method to avoid ambiguity
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Use doAnswer to specify the method explicitly to avoid ambiguity
    doAnswer(invocation -> expectedResult)
        .when(spyGson)
        .fromJson(
            argThat(arg -> arg == jsonElement),
            eq(expectedTypeToken));

    // Act
    Method methodUnderTest = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Type.class);
    methodUnderTest.setAccessible(true);
    @SuppressWarnings("unchecked")
    String actualResult = (String) methodUnderTest.invoke(spyGson, jsonElement, type);

    // Assert
    assertEquals(expectedResult, actualResult);
    verify(spyGson).fromJson(eq(jsonElement), eq(expectedTypeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndType_nullJsonElement_shouldPassNullToDelegate() throws Exception {
    // Arrange
    JsonElement jsonElement = null;
    Type type = Integer.class;
    @SuppressWarnings("rawtypes")
    TypeToken expectedTypeToken = TypeToken.get(type);

    Gson spyGson = spy(gson);
    Integer expectedResult = 42;

    // Use reflection to get the exact method to avoid ambiguity
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Use doAnswer to specify the method explicitly to avoid ambiguity
    doAnswer(invocation -> expectedResult)
        .when(spyGson)
        .fromJson(
            isNull(),
            eq(expectedTypeToken));

    // Act
    Method methodUnderTest = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Type.class);
    methodUnderTest.setAccessible(true);
    Integer actualResult = (Integer) methodUnderTest.invoke(spyGson, jsonElement, type);

    // Assert
    assertEquals(expectedResult, actualResult);
    verify(spyGson).fromJson(isNull(), eq(expectedTypeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndType_nullType_shouldThrowNullPointerException() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Type nullType = null;

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Type.class);
    fromJsonMethod.setAccessible(true);

    // Act & Assert
    Exception exception = assertThrows(Exception.class, () -> {
      fromJsonMethod.invoke(gson, jsonElement, nullType);
    });
    // The actual cause should be NullPointerException
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}