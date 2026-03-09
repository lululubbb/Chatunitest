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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLongArray;

class GsonAtomicLongArrayAdapterTest {

  private Gson gson;
  private TypeAdapter<Number> longAdapterMock;
  private TypeAdapter<AtomicLongArray> atomicLongArrayAdapter;

  @BeforeEach
  @SuppressWarnings("unchecked")
  public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    gson = new Gson();

    // Mock TypeAdapter<Number>
    longAdapterMock = mock(TypeAdapter.class);

    // Access private static method atomicLongArrayAdapter via reflection
    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    atomicLongArrayAdapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapterMock);
  }

  @Test
    @Timeout(8000)
  public void testWrite_emptyArray() throws IOException {
    AtomicLongArray array = new AtomicLongArray(0);

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    atomicLongArrayAdapter.write(jsonWriter, array);
    jsonWriter.close();

    String json = stringWriter.toString();
    assertEquals("[]", json);

    // Verify longAdapter.write is never called for empty array
    verify(longAdapterMock, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  public void testWrite_nonEmptyArray() throws IOException {
    AtomicLongArray array = new AtomicLongArray(3);
    array.set(0, 10L);
    array.set(1, 20L);
    array.set(2, 30L);

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    atomicLongArrayAdapter.write(jsonWriter, array);
    jsonWriter.close();

    String json = stringWriter.toString();
    assertEquals("[10,20,30]", json);

    // Verify longAdapter.write called 3 times with correct values
    ArgumentCaptor<JsonWriter> writerCaptor = ArgumentCaptor.forClass(JsonWriter.class);
    ArgumentCaptor<Number> numberCaptor = ArgumentCaptor.forClass(Number.class);
    verify(longAdapterMock, times(3)).write(writerCaptor.capture(), numberCaptor.capture());

    assertEquals(3, numberCaptor.getAllValues().size());
    assertEquals(10L, numberCaptor.getAllValues().get(0).longValue());
    assertEquals(20L, numberCaptor.getAllValues().get(1).longValue());
    assertEquals(30L, numberCaptor.getAllValues().get(2).longValue());
  }

  @Test
    @Timeout(8000)
  public void testRead_emptyArray() throws IOException {
    String json = "[]";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    // Setup longAdapter.read to throw if called (should not be called)
    when(longAdapterMock.read(any())).thenThrow(new RuntimeException("Should not be called"));

    AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

    assertNotNull(result);
    assertEquals(0, result.length());
  }

  @Test
    @Timeout(8000)
  public void testRead_nonEmptyArray() throws IOException {
    String json = "[10,20,30]";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    // Setup longAdapter.read to read numbers from JsonReader
    // We mock longAdapter.read to read the next long value from JsonReader
    when(longAdapterMock.read(any())).thenAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      if (in.peek() == JsonToken.NUMBER) {
        return in.nextLong();
      }
      throw new IOException("Expected NUMBER token");
    });

    AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

    assertNotNull(result);
    assertEquals(3, result.length());
    assertEquals(10L, result.get(0));
    assertEquals(20L, result.get(1));
    assertEquals(30L, result.get(2));
  }

  @Test
    @Timeout(8000)
  public void testRead_invalidJson_throwsIOException() throws IOException {
    String json = "[10, \"string\", 30]";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    // Setup longAdapter.read to throw IOException on invalid token (string instead of number)
    when(longAdapterMock.read(any())).thenAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      JsonToken token = in.peek();
      if (token == JsonToken.NUMBER) {
        return in.nextLong();
      }
      throw new IOException("Expected NUMBER token but got " + token);
    });

    IOException exception = assertThrows(IOException.class, () -> {
      atomicLongArrayAdapter.read(jsonReader);
    });

    assertTrue(exception.getMessage().contains("Expected NUMBER token"));
  }

  @Test
    @Timeout(8000)
  public void testWrite_nullValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    // The adapter is nullSafe(), so it should write null JSON literal
    atomicLongArrayAdapter.write(jsonWriter, null);
    jsonWriter.close();

    String json = stringWriter.toString();
    assertEquals("null", json);

    // longAdapter.write should not be called
    verify(longAdapterMock, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  public void testRead_nullValue() throws IOException {
    String json = "null";
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    // Setup longAdapter.read to throw if called (should not be called for null)
    when(longAdapterMock.read(any())).thenThrow(new RuntimeException("Should not be called"));

    AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

    assertNull(result);
  }
}