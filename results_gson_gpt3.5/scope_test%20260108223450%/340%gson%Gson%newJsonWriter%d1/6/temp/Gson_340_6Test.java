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
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class GsonNewJsonWriterTest {

  private Gson gson;
  private String jsonNonExecutablePrefix;

  @BeforeEach
  void setUp() {
    gson = new Gson();

    // Access private static final String JSON_NON_EXECUTABLE_PREFIX via reflection
    try {
      Field prefixField = Gson.class.getDeclaredField("JSON_NON_EXECUTABLE_PREFIX");
      prefixField.setAccessible(true);
      jsonNonExecutablePrefix = (String) prefixField.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // Set default fields to false or true according to defaults, then override in tests
    setField(gson, "generateNonExecutableJson", false);
    setField(gson, "prettyPrinting", false);
    setField(gson, "htmlSafe", true);
    setField(gson, "lenient", false);
    setField(gson, "serializeNulls", false);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = Gson.class.getDeclaredField(fieldName);
      field.setAccessible(true);

      // Remove final modifier if present
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_DefaultSettings_WritesNoPrefixAndSetsProperties() throws IOException {
    // Use a real StringWriter wrapped by a Mockito spy that delegates calls properly
    StringWriter stringWriter = spy(new StringWriter());

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // No prefix written
    verify(stringWriter, never()).write(jsonNonExecutablePrefix);

    assertNotNull(jsonWriter);
    // Indent should not be set (default "")
    assertEquals("", getIndent(jsonWriter));
    assertTrue(jsonWriter.isHtmlSafe());
    assertFalse(jsonWriter.isLenient());
    assertFalse(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_GenerateNonExecutableJson_WritesPrefix() throws IOException {
    setField(gson, "generateNonExecutableJson", true);
    // Use a real StringWriter wrapped by a Mockito spy that delegates calls properly
    StringWriter stringWriter = spy(new StringWriter());

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // Verify prefix written (write(String) is called with the prefix)
    verify(stringWriter).write(jsonNonExecutablePrefix);

    assertNotNull(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_PrettyPrinting_SetsIndent() throws IOException {
    setField(gson, "prettyPrinting", true);
    StringWriter stringWriter = new StringWriter();

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    assertEquals("  ", getIndent(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_HtmlSafeFalse_SetsHtmlSafeFalse() throws IOException {
    setField(gson, "htmlSafe", false);
    StringWriter stringWriter = new StringWriter();

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    assertFalse(jsonWriter.isHtmlSafe());
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_LenientTrue_SetsLenientTrue() throws IOException {
    setField(gson, "lenient", true);
    StringWriter stringWriter = new StringWriter();

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    assertTrue(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_SerializeNullsTrue_SetsSerializeNullsTrue() throws IOException {
    setField(gson, "serializeNulls", true);
    StringWriter stringWriter = new StringWriter();

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    assertTrue(jsonWriter.getSerializeNulls());
  }

  private String getIndent(JsonWriter jsonWriter) {
    try {
      Field indentField = JsonWriter.class.getDeclaredField("indent");
      indentField.setAccessible(true);
      return (String) indentField.get(jsonWriter);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}