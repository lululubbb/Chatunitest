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
import com.google.gson.internal.Primitives;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonAndClass_returnsObject() throws Exception {
    String json = "{\"dummy\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<DummyClass> clazz = DummyClass.class;

    // Spy gson to mock fromJson(Reader, TypeToken) call
    Gson spyGson = Mockito.spy(gson);
    DummyClass dummyInstance = new DummyClass();
    TypeToken<DummyClass> typeToken = TypeToken.get(clazz);

    // Mock the fromJson(Reader, TypeToken) method on spyGson with any Reader and the expected TypeToken
    doReturn(dummyInstance).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    // Mock static Primitives.wrap to return the wrapped class (simulate real behavior)
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      DummyClass result = spyGson.fromJson(reader, clazz);

      primitivesMockedStatic.verify(() -> Primitives.wrap(clazz));
      assertNotNull(result);
      assertSame(dummyInstance, result);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_callsPrimitivesWrapAndCast() throws Exception {
    String json = "{\"dummy\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<DummyClass> clazz = DummyClass.class;

    Gson spyGson = Mockito.spy(gson);
    DummyClass dummyInstance = new DummyClass();
    TypeToken<DummyClass> typeToken = TypeToken.get(clazz);

    doReturn(dummyInstance).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      DummyClass result = spyGson.fromJson(reader, clazz);

      primitivesMockedStatic.verify(() -> Primitives.wrap(clazz));
      assertSame(dummyInstance, result);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullReader_throwsException() throws Throwable {
    // To resolve ambiguity, call fromJson(JsonReader, Type) explicitly via reflection
    Method fromJsonMethod = Gson.class.getMethod("fromJson", com.google.gson.stream.JsonReader.class, Type.class);

    assertThrows(NullPointerException.class, () -> {
      try {
        fromJsonMethod.invoke(gson, (Object) null, DummyClass.class);
      } catch (Exception e) {
        // unwrap invocation target exception
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullClass_throwsException() throws Throwable {
    Reader reader = new StringReader("{}");

    // To resolve ambiguity, call fromJson(Reader, Type) explicitly via reflection
    Method fromJsonMethod = Gson.class.getMethod("fromJson", Reader.class, Type.class);

    assertThrows(NullPointerException.class, () -> {
      try {
        fromJsonMethod.invoke(gson, reader, (Object) null);
      } catch (Exception e) {
        // unwrap invocation target exception
        throw e.getCause();
      }
    });
  }

  // DummyClass for testing
  static class DummyClass {
    String dummy;
  }
}