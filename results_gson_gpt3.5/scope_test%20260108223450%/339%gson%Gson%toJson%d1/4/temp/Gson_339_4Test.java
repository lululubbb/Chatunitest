package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldWriteJsonUsingJsonWriter() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    StringWriter stringWriter = new StringWriter();

    Gson spyGson = Mockito.spy(gson);

    // Spy newJsonWriter to return a JsonWriter wrapping the stringWriter
    doReturn(new JsonWriter(stringWriter)).when(spyGson).newJsonWriter(any());

    // Spy toJson(JsonElement, JsonWriter) to write a string to stringWriter
    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(1);
      writer.jsonValue("\"mocked\"");
      writer.flush();
      return null;
    }).when(spyGson).toJson(eq(jsonElement), any(JsonWriter.class));

    spyGson.toJson(jsonElement, stringWriter);
    assertEquals("\"mocked\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldThrowJsonIOException_whenIOExceptionOccurs() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    Appendable throwingAppendable = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("append failed");
      }
      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("append failed");
      }
      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("append failed");
      }
    };

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      gson.toJson(jsonElement, throwingAppendable);
    });
    assertTrue(exception.getCause() instanceof IOException);
    assertEquals("append failed", exception.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void toJson_privateNewJsonWriter_invocationViaReflection() throws Exception {
    StringWriter stringWriter = new StringWriter();
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, stringWriter);
    assertNotNull(jsonWriter);

    // Use jsonWriter to write a simple JSON string and verify output
    jsonWriter.beginObject();
    jsonWriter.name("key").value("value");
    jsonWriter.endObject();
    jsonWriter.flush();

    assertEquals("{\"key\":\"value\"}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void toJson_privateToJson_invocationViaReflection() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    StringWriter stringWriter = new StringWriter();

    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);
    JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, stringWriter);

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    // We just verify that invoking the private method does not throw exceptions
    toJsonMethod.invoke(gson, jsonElement, jsonWriter);

    jsonWriter.flush(); // ensure any buffered output is written

    // Because jsonElement is mock and no real calls expected, no interactions with jsonWriter expected
    // jsonWriter is not a mock, so verifyNoInteractions cannot be used on it
    // Instead, verify that the stringWriter is still empty (no output)
    assertEquals("", stringWriter.toString());
  }
}