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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_validJsonElement_appendsJson() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    StringWriter writer = new StringWriter();

    // Spy gson to mock newJsonWriter and toJson(JsonElement, JsonWriter)
    Gson spyGson = Mockito.spy(gson);
    JsonWriter jsonWriter = new JsonWriter(writer);
    doReturn(jsonWriter).when(spyGson).newJsonWriter(any());

    doAnswer(invocation -> {
      JsonWriter argJsonWriter = invocation.getArgument(1);
      argJsonWriter.value("test");
      argJsonWriter.flush();
      return null;
    }).when(spyGson).toJson(any(JsonElement.class), any(JsonWriter.class));

    // Call the method under test on the spy
    spyGson.toJson(jsonElement, (Appendable) writer);

    assertEquals("\"test\"", writer.toString());
  }

  @Test
    @Timeout(8000)
  void toJson_ioException_throwsJsonIOException() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    Appendable writer = mock(Appendable.class);

    // Spy gson to mock newJsonWriter throwing IOException
    Gson spyGson = Mockito.spy(gson);
    doThrow(new IOException("io error")).when(spyGson).newJsonWriter(any());

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      spyGson.toJson(jsonElement, writer);
    });

    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("io error", thrown.getCause().getMessage());
  }
}