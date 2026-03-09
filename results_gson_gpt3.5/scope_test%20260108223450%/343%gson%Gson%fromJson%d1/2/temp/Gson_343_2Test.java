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

class GsonFromJsonStringClassTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonAndClass_returnsObject() throws Exception {
    String json = "{\"value\":42}";
    Class<Dummy> classOfT = Dummy.class;

    // We spy on gson to mock fromJson(String, TypeToken<T>)
    Gson spyGson = Mockito.spy(gson);
    Dummy dummy = new Dummy();
    dummy.value = 42;

    // Mock fromJson(String, TypeToken<T>) to return dummy instance
    doReturn(dummy).when(spyGson).fromJson(eq(json), any(TypeToken.class));

    // Mock Primitives.wrap to return classOfT itself (since Dummy is not primitive)
    try (MockedStatic<Primitives> mockedPrimitives = mockStatic(Primitives.class)) {
      mockedPrimitives.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      Dummy result = spyGson.fromJson(json, classOfT);

      assertNotNull(result);
      assertEquals(42, result.value);
      verify(spyGson).fromJson(eq(json), any(TypeToken.class));
      mockedPrimitives.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJson_returnsNull() throws Exception {
    String json = null;
    Class<Dummy> classOfT = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    doReturn(null).when(spyGson).fromJson(eq(json), any(TypeToken.class));

    try (MockedStatic<Primitives> mockedPrimitives = mockStatic(Primitives.class)) {
      mockedPrimitives.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      Dummy result = spyGson.fromJson(json, classOfT);

      assertNull(result);
      verify(spyGson).fromJson(eq(json), any(TypeToken.class));
      mockedPrimitives.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withPrimitiveClass_wrapsAndCasts() throws Exception {
    String json = "123";
    Class<Integer> classOfT = int.class;

    Gson spyGson = Mockito.spy(gson);
    Integer value = 123;

    doReturn(value).when(spyGson).fromJson(eq(json), any(TypeToken.class));

    try (MockedStatic<Primitives> mockedPrimitives = mockStatic(Primitives.class)) {
      // Primitives.wrap(int.class) returns Integer.class
      mockedPrimitives.when(() -> Primitives.wrap(classOfT)).thenReturn(Integer.class);

      Integer result = spyGson.fromJson(json, classOfT);

      assertNotNull(result);
      assertEquals(123, result);
      verify(spyGson).fromJson(eq(json), any(TypeToken.class));
      mockedPrimitives.verify(() -> Primitives.wrap(classOfT));
    }
  }

  static class Dummy {
    public int value;
  }
}