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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

class GsonNewJsonReaderTest {

  private Gson gson;

  @BeforeEach
  void setUp() throws Exception {
    // Create Gson instance with lenient = true via GsonBuilder
    gson = new GsonBuilder().create();

    // Use reflection to set private final lenient field to true
    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);

    // Remove final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(lenientField, lenientField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    lenientField.setBoolean(gson, true);
  }

  @Test
    @Timeout(8000)
  void testNewJsonReader_withLenientTrue() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonReader jsonReader = gson.newJsonReader(reader);

    assertNotNull(jsonReader);
    // lenient should be true as set in setUp()
    assertTrue(jsonReader.isLenient());

    // Test that the JsonReader reads the JSON correctly
    assertEquals(JsonToken.BEGIN_OBJECT, jsonReader.peek());
  }

  @Test
    @Timeout(8000)
  void testNewJsonReader_withLenientFalse() throws Exception {
    // Create Gson instance with lenient = false (default)
    Gson gsonLenientFalse = new GsonBuilder().create();

    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonReader jsonReader = gsonLenientFalse.newJsonReader(reader);

    assertNotNull(jsonReader);
    // lenient should be false here
    assertFalse(jsonReader.isLenient());

    assertEquals(JsonToken.BEGIN_OBJECT, jsonReader.peek());
  }

  @Test
    @Timeout(8000)
  void testNewJsonReader_withMockedReader() throws Exception {
    Reader mockReader = mock(Reader.class);

    JsonReader jsonReader = gson.newJsonReader(mockReader);

    assertNotNull(jsonReader);
    // The lenient flag is true from setUp()
    assertTrue(jsonReader.isLenient());
  }
}