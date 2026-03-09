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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;

class GsonFromJsonTest {

  private Gson gson;
  private JsonReader reader;
  private TypeToken<String> typeToken;
  private TypeAdapter<String> typeAdapter;

  @BeforeEach
  void setUp() {
    gson = spy(new Gson());
    reader = mock(JsonReader.class);
    typeToken = TypeToken.get(String.class);
    typeAdapter = mock(TypeAdapter.class);
  }

  @Test
    @Timeout(8000)
  void fromJson_successfulRead_returnsValue() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    doReturn(typeAdapter).when(gson).getAdapter(typeToken);
    when(typeAdapter.read(reader)).thenReturn("value");

    String result = gson.fromJson(reader, typeToken);

    assertEquals("value", result);
    InOrder inOrder = inOrder(reader, gson, typeAdapter);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(gson).getAdapter(typeToken);
    inOrder.verify(typeAdapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_emptyDocument_returnsNull() throws Exception {
    when(reader.isLenient()).thenReturn(true);
    doThrow(new EOFException()).when(reader).peek();

    String result = gson.fromJson(reader, typeToken);

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
    when(reader.isLenient()).thenReturn(true);
    doThrow(new EOFException()).when(reader).peek();
    // Simulate peek called once successfully, then second call throws EOFException
    doAnswer(invocation -> {
      throw new EOFException();
    }).when(reader).peek();

    // To simulate isEmpty = false, we call peek once normally then throw
    doNothing().when(reader).setLenient(anyBoolean());
    // We simulate peek success then throw by overriding peek call sequence
    // but it's complicated, so instead we simulate isEmpty false by calling peek once before fromJson
    // so here we simulate that peek throws immediately, so isEmpty true, so null returned
    // For coverage we test throwing JsonSyntaxException by calling fromJson with a spy on peek method

    // Instead, simulate peek throwing EOFException after isEmpty false:
    Gson spyGson = spy(gson);
    when(reader.isLenient()).thenReturn(true);
    doReturn(true).when(reader).isLenient();
    doAnswer(invocation -> {
      // first call peek returns something, second call throws EOFException
      if (invocation.getMock() == reader) {
        throw new EOFException();
      }
      return JsonToken.STRING;
    }).when(reader).peek();

    // We can't easily simulate this with mocks due to peek called once in try block
    // Instead, test coverage of catch block by calling fromJson with a reader that throws EOFException after peek
    // So we directly test that JsonSyntaxException is thrown when EOFException thrown and isEmpty false

    // We'll do this by creating a JsonReader subclass that throws EOFException on peek after first peek
    class TestJsonReader extends JsonReader {
      private boolean firstPeek = true;
      public TestJsonReader() { super(new java.io.StringReader("")); }
      @Override
      public JsonToken peek() throws IOException {
        if (firstPeek) {
          firstPeek = false;
          return JsonToken.BEGIN_OBJECT;
        } else {
          throw new EOFException();
        }
      }
      @Override
      public boolean isLenient() { return true; }
      @Override
      public void setLenient(boolean lenient) {}
    }

    JsonReader testReader = new TestJsonReader();

    TypeAdapter<String> adapter = mock(TypeAdapter.class);
    Gson gsonSpy = spy(new Gson());
    doReturn(adapter).when(gsonSpy).getAdapter(any());

    // simulate adapter.read throwing EOFException to trigger catch
    doThrow(new EOFException()).when(adapter).read(testReader);

    assertThrows(JsonSyntaxException.class, () -> gsonSpy.fromJson(testReader, typeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_illegalStateException_throwsJsonSyntaxException() throws Exception {
    when(reader.isLenient()).thenReturn(true);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    doReturn(typeAdapter).when(gson).getAdapter(typeToken);
    when(typeAdapter.read(reader)).thenThrow(new IllegalStateException("illegal"));

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, typeToken));
    assertTrue(ex.getCause() instanceof IllegalStateException);
    assertEquals("illegal", ex.getCause().getMessage());
    verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_ioException_throwsJsonSyntaxException() throws Exception {
    when(reader.isLenient()).thenReturn(true);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    doReturn(typeAdapter).when(gson).getAdapter(typeToken);
    when(typeAdapter.read(reader)).thenThrow(new IOException("io"));

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, typeToken));
    assertTrue(ex.getCause() instanceof IOException);
    assertEquals("io", ex.getCause().getMessage());
    verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_assertionError_throwsAssertionErrorWithMessage() throws Exception {
    when(reader.isLenient()).thenReturn(true);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    doReturn(typeAdapter).when(gson).getAdapter(typeToken);
    AssertionError error = new AssertionError("assert");
    when(typeAdapter.read(reader)).thenThrow(error);

    AssertionError thrown = assertThrows(AssertionError.class, () -> gson.fromJson(reader, typeToken));
    assertTrue(thrown.getMessage().contains("AssertionError (GSON "));
    assertEquals(error, thrown.getCause());
    verify(reader).setLenient(false);
  }
}