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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonFromJsonStringClassTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = spy(new Gson());
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonAndClass_shouldReturnObject() throws Exception {
    String json = "{\"field\":\"value\"}";
    Class<?> clazz = String.class;
    Object expectedObject = "mockedObject";

    // Mock fromJson(String, TypeToken) to return expectedObject
    doReturn(expectedObject).when(gson).fromJson(eq(json), any(TypeToken.class));

    // Mock static Primitives.wrap to return the class itself (String.class)
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      Object actual = gson.fromJson(json, clazz);

      assertNotNull(actual);
      assertEquals(expectedObject, actual);
      assertSame(clazz, Primitives.wrap(clazz));
      verify(gson).fromJson(eq(json), any(TypeToken.class));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJson_shouldReturnNull() throws Exception {
    String json = null;
    Class<?> clazz = String.class;

    doReturn(null).when(gson).fromJson(eq(json), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      Object actual = gson.fromJson(json, clazz);

      assertNull(actual);
      verify(gson).fromJson(eq(json), any(TypeToken.class));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withPrimitiveClass_shouldCastCorrectly() throws Exception {
    String json = "123";
    Class<Integer> clazz = int.class;
    Integer expectedObject = 123;

    doReturn(expectedObject).when(gson).fromJson(eq(json), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(Integer.class);

      Integer actual = gson.fromJson(json, clazz);

      assertNotNull(actual);
      assertEquals(expectedObject, actual);
      verify(gson).fromJson(eq(json), any(TypeToken.class));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_whenFromJsonThrowsJsonSyntaxException_shouldPropagate() {
    String json = "invalid json";
    Class<String> clazz = String.class;

    doThrow(new JsonSyntaxException("syntax error")).when(gson).fromJson(eq(json), any(TypeToken.class));

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, clazz));
  }
}