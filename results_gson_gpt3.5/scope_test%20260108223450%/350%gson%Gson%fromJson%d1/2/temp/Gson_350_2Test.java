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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

class Gson_fromJson_Test {
  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonReaderAndType_shouldReturnParsedObject() throws IOException {
    String json = "{\"name\":\"test\"}";
    Reader stringReader = new StringReader(json);
    JsonReader jsonReader = new JsonReader(stringReader);
    Type type = String.class;

    // Spy on gson to verify delegation to fromJson(JsonReader, TypeToken)
    Gson spyGson = Mockito.spy(gson);

    // Call the focal method
    spyGson.fromJson(jsonReader, type);

    // Verify that fromJson(JsonReader, TypeToken) was called with the correct TypeToken
    verify(spyGson).fromJson(eq(jsonReader), argThat(new ArgumentMatcher<TypeToken<?>>() {
      @Override
      public boolean matches(TypeToken<?> argument) {
        return argument != null && argument.getType().equals(type);
      }
    }));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJsonReader_shouldThrowNullPointerException() {
    Type type = String.class;
    assertThrows(NullPointerException.class, () -> gson.fromJson((JsonReader) null, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullType_shouldThrowNullPointerException() {
    String json = "{}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    assertThrows(NullPointerException.class, () -> gson.fromJson(jsonReader, (Type) null));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withMalformedJson_shouldThrowJsonSyntaxException() {
    String malformedJson = "{name:\"missingQuotes}";
    JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withIOException_shouldThrowJsonIOException() {
    // Create a JsonReader that throws IOException on read
    Reader failingReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("Simulated IO exception");
      }

      @Override
      public void close() throws IOException {}
    };
    JsonReader jsonReader = new JsonReader(failingReader);
    Type type = Object.class;

    assertThrows(JsonIOException.class, () -> gson.fromJson(jsonReader, type));
  }
}