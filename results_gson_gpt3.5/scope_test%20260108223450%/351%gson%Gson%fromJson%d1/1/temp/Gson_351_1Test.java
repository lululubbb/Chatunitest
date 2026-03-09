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

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.EOFException;
import java.io.IOException;

class GsonFromJsonTest {

  private Gson gson;
  private JsonReader reader;
  private TypeToken<String> typeToken;
  private Gson spyGson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    reader = mock(JsonReader.class);
    typeToken = TypeToken.get(String.class);
    spyGson = spy(gson);
  }

  @Test
    @Timeout(8000)
  void fromJson_successfulRead_returnsValue() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(false);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    @SuppressWarnings("unchecked")
    var typeAdapter = mock(com.google.gson.TypeAdapter.class);
    when(typeAdapter.read(reader)).thenReturn("hello");
    doReturn(typeAdapter).when(spyGson).getAdapter(typeToken);

    // Act
    String result = spyGson.fromJson(reader, typeToken);

    // Assert
    assertEquals("hello", result);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_emptyDocument_returnsNull() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(true);
    // Simulate peek throwing EOFException on first call to simulate empty document
    doThrow(new EOFException()).when(reader).peek();

    // Act
    String result = gson.fromJson(reader, typeToken);

    // Assert
    assertNull(result);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(reader).setLenient(true);
  }

  @Test
    @Timeout(8000)
  void fromJson_eofExceptionNotEmpty_throwsJsonSyntaxException() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(false);
    JsonReader readerSpy = spy(reader);
    when(readerSpy.isLenient()).thenReturn(false);
    // We need peek() to throw EOFException, but isLenient() called before peek(), so peek() first call throws EOFException
    // and isLenient() returns false.
    doThrow(new EOFException()).when(readerSpy).peek();

    // Act & Assert
    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(readerSpy, typeToken);
    });
    assertTrue(ex.getCause() instanceof EOFException);
  }

  @Test
    @Timeout(8000)
  void fromJson_illegalStateException_throwsJsonSyntaxException() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(false);
    doThrow(new IllegalStateException()).when(reader).peek();

    // Act & Assert
    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, typeToken);
    });
    assertTrue(ex.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void fromJson_ioException_throwsJsonSyntaxException() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(false);
    doThrow(new IOException()).when(reader).peek();

    // Act & Assert
    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, typeToken);
    });
    assertTrue(ex.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  void fromJson_assertionError_throwsAssertionErrorWithMessage() throws Exception {
    // Arrange
    when(reader.isLenient()).thenReturn(false);
    doThrow(new AssertionError("fail")).when(reader).peek();

    // Act & Assert
    AssertionError error = assertThrows(AssertionError.class, () -> {
      gson.fromJson(reader, typeToken);
    });
    assertTrue(error.getMessage().contains("AssertionError (GSON"));
    assertTrue(error.getMessage().contains("fail"));
  }
}