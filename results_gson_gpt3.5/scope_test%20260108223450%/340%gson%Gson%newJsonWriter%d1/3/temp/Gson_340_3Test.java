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
import java.io.StringWriter;
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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class GsonNewJsonWriterTest {

  private Gson gson;
  private Writer mockWriter;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    mockWriter = mock(Writer.class);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_defaultSettings_noPrefixWrittenAndJsonWriterConfigured() throws IOException {
    // Access private final fields via reflection and remove final modifier to set them
    setFinalField(gson, "generateNonExecutableJson", false);
    setFinalField(gson, "prettyPrinting", false);
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "lenient", false);
    setFinalField(gson, "serializeNulls", false);

    JsonWriter jsonWriter = gson.newJsonWriter(mockWriter);

    // No prefix should be written
    verify(mockWriter, never()).write(anyString());

    // JsonWriter should be configured with default values
    assertNotNull(jsonWriter);

    // Check JsonWriter configuration via reflection
    assertEquals(false, getJsonWriterField(jsonWriter, "lenient"));
    assertEquals(true, getJsonWriterField(jsonWriter, "htmlSafe"));
    assertEquals(false, getJsonWriterField(jsonWriter, "serializeNulls"));
    assertEquals("", getJsonWriterField(jsonWriter, "indent"));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_generateNonExecutableJson_prefixWritten() throws IOException {
    setFinalField(gson, "generateNonExecutableJson", true);
    setFinalField(gson, "prettyPrinting", false);
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "lenient", false);
    setFinalField(gson, "serializeNulls", false);

    JsonWriter jsonWriter = gson.newJsonWriter(mockWriter);

    String prefix = getGsonPrivateStaticField("JSON_NON_EXECUTABLE_PREFIX");
    verify(mockWriter).write(prefix);
    assertNotNull(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_prettyPrinting_setsIndent() throws IOException {
    setFinalField(gson, "generateNonExecutableJson", false);
    setFinalField(gson, "prettyPrinting", true);
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "lenient", false);
    setFinalField(gson, "serializeNulls", false);

    JsonWriter jsonWriter = gson.newJsonWriter(mockWriter);

    // Indent should be set to two spaces
    assertEquals("  ", getJsonWriterField(jsonWriter, "indent"));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_allFlagsSet_correctConfiguration() throws IOException {
    setFinalField(gson, "generateNonExecutableJson", true);
    setFinalField(gson, "prettyPrinting", true);
    setFinalField(gson, "htmlSafe", false);
    setFinalField(gson, "lenient", true);
    setFinalField(gson, "serializeNulls", true);

    JsonWriter jsonWriter = gson.newJsonWriter(mockWriter);

    String prefix = getGsonPrivateStaticField("JSON_NON_EXECUTABLE_PREFIX");
    verify(mockWriter).write(prefix);
    assertEquals("  ", getJsonWriterField(jsonWriter, "indent"));
    assertEquals(false, getJsonWriterField(jsonWriter, "htmlSafe"));
    assertEquals(true, getJsonWriterField(jsonWriter, "lenient"));
    assertEquals(true, getJsonWriterField(jsonWriter, "serializeNulls"));
  }

  private void setFinalField(Object target, String fieldName, Object value) {
    try {
      Field field = Gson.class.getDeclaredField(fieldName);
      field.setAccessible(true);

      // Remove final modifier from field
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object getJsonWriterField(JsonWriter jsonWriter, String fieldName) {
    try {
      Field field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(jsonWriter);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String getGsonPrivateStaticField(String fieldName) {
    try {
      Field field = Gson.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (String) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}