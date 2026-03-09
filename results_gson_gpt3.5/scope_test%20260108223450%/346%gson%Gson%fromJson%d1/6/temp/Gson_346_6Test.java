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
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJson_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldReturnObjectOfClass_whenJsonIsValid() throws Exception {
    String json = "{\"dummy\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;

    Dummy dummyInstance = new Dummy();
    // Spy on gson to mock fromJson(Reader, TypeToken<T>)
    Gson spyGson = Mockito.spy(gson);

    // Create TypeToken for Dummy
    TypeToken<Dummy> typeToken = TypeToken.get(clazz);

    // Mock the fromJson(Reader, TypeToken<T>) call to return dummyInstance
    doReturn(dummyInstance).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    // Mock Primitives.wrap(clazz) to return clazz normally
    try (MockedStatic<Primitives> primitivesStatic = mockStatic(Primitives.class)) {
      primitivesStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      // Call the real fromJson(Reader, Class<T>) method on spyGson to trigger the mocked fromJson(Reader, TypeToken<T>)
      Dummy result = spyGson.fromJson(reader, clazz);

      assertNotNull(result);
      assertSame(dummyInstance, result);
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowJsonSyntaxException_whenFromJsonThrows() throws Exception {
    String json = "{\"dummy\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    TypeToken<Dummy> typeToken = TypeToken.get(clazz);

    doThrow(new JsonSyntaxException("syntax error")).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, clazz);
    });
    assertEquals("syntax error", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowJsonIOException_whenFromJsonThrows() throws Exception {
    String json = "{\"dummy\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    TypeToken<Dummy> typeToken = TypeToken.get(clazz);

    doThrow(new JsonIOException("io error")).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      spyGson.fromJson(reader, clazz);
    });
    assertEquals("io error", exception.getMessage());
  }

  // Dummy class for testing
  static class Dummy {
    String dummy;
  }
}