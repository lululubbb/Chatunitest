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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
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
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonReaderTypeTokenTest {
  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_Reader_Type_Success() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Type type = new TypeToken<TestClass>() {}.getType();

    TestClass result = gson.fromJson(reader, type);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_Reader_Type_JsonSyntaxException() throws Exception {
    String invalidJson = "{name:\"test\""; // malformed JSON
    Reader reader = new StringReader(invalidJson);
    Type type = new TypeToken<TestClass>() {}.getType();

    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, type);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_Reader_Type_JsonIOException() throws Exception {
    Reader mockReader = mock(Reader.class);
    Type type = new TypeToken<TestClass>() {}.getType();

    doThrow(new IOException("IO error")).when(mockReader).read(any(char[].class), anyInt(), anyInt());

    assertThrows(JsonIOException.class, () -> {
      gson.fromJson(mockReader, type);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_Reader_Type_InvokesPrivateFromJson() throws Exception {
    String json = "{\"name\":\"reflection\"}";
    Reader reader = new StringReader(json);
    Type type = new TypeToken<TestClass>() {}.getType();

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    JsonReader jsonReader = new JsonReader(reader);
    Object result = fromJsonMethod.invoke(gson, jsonReader, TypeToken.get(type));
    assertNotNull(result);
    assertTrue(result instanceof TestClass);
    assertEquals("reflection", ((TestClass) result).name);
  }

  static class TestClass {
    String name;
  }
}