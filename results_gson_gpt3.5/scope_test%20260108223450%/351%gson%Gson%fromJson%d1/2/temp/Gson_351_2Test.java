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
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.EOFException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class Gson_FromJson_Test {

  private Gson gson;
  private JsonReader reader;
  private TypeToken<String> typeToken;
  private Gson spyGson;

  private int peekCallCount = 0;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
    reader = mock(JsonReader.class);
    typeToken = TypeToken.get(String.class);
    spyGson = spy(gson);
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldReturnNull_whenEmptyDocument() throws IOException {
    when(reader.isLenient()).thenReturn(false);
    // peek() throws EOFException on empty document
    doThrow(new EOFException()).when(reader).peek();

    String result = spyGson.fromJson(reader, typeToken);

    assertNull(result);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldThrowJsonSyntaxException_onEOFException_whenNotEmpty() throws IOException {
    when(reader.isLenient()).thenReturn(false);

    peekCallCount = 0;

    @SuppressWarnings("unchecked")
    com.google.gson.TypeAdapter<String> adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);

    doAnswer(invocation -> {
      if (peekCallCount == 0) {
        peekCallCount++;
        return JsonToken.BEGIN_OBJECT;
      } else {
        throw new EOFException();
      }
    }).when(reader).peek();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof EOFException);
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldThrowJsonSyntaxException_onIllegalStateException() throws IOException {
    when(reader.isLenient()).thenReturn(false);
    doThrow(new IllegalStateException("illegal state")).when(reader).peek();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("illegal state", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldThrowJsonSyntaxException_onIOException() throws IOException {
    when(reader.isLenient()).thenReturn(false);
    doThrow(new IOException("io exception")).when(reader).peek();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("io exception", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldThrowAssertionError_withMessage() throws IOException {
    when(reader.isLenient()).thenReturn(false);
    AssertionError error = new AssertionError("assertion failed");
    doThrow(error).when(reader).peek();

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertTrue(thrown.getMessage().contains("GSON"));
    assertTrue(thrown.getMessage().contains("assertion failed"));
    assertEquals(error, thrown.getCause());
  }

  @Test
    @Timeout(8000)
  public void fromJson_shouldReturnDeserializedObject_whenNoException() throws IOException {
    when(reader.isLenient()).thenReturn(false);
    when(reader.peek()).thenReturn(JsonToken.STRING);

    @SuppressWarnings("unchecked")
    com.google.gson.TypeAdapter<String> adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    when(adapter.read(reader)).thenReturn("deserialized");

    String result = spyGson.fromJson(reader, typeToken);

    assertEquals("deserialized", result);
    InOrder inOrder = inOrder(reader, adapter, spyGson);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(spyGson).getAdapter(typeToken);
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }
}