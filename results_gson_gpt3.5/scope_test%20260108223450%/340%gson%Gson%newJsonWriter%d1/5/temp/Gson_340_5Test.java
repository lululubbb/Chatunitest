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

class Gson_newJsonWriter_Test {

  private Gson gson;
  private Writer writer;

  @BeforeEach
  void setUp() throws Exception {
    // Use default constructor to initialize gson
    gson = new Gson();

    writer = mock(Writer.class);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_generateNonExecutableJsonTrue_writesPrefixAndSetsProperties() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Use reflection to set private final field generateNonExecutableJson to true
    setFinalField(gson, "generateNonExecutableJson", true);

    // Set other fields for coverage
    setFinalField(gson, "prettyPrinting", true);
    setFinalField(gson, "htmlSafe", false);
    setFinalField(gson, "lenient", true);
    setFinalField(gson, "serializeNulls", true);

    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Verify prefix written
    verify(writer).write(")]}'\n");

    // Verify JsonWriter properties
    assertEquals("  ", getJsonWriterIndent(jsonWriter));
    assertFalse(getJsonWriterHtmlSafe(jsonWriter));
    assertTrue(getJsonWriterLenient(jsonWriter));
    assertTrue(getJsonWriterSerializeNulls(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_generateNonExecutableJsonFalse_noPrefixWritten() throws IOException, NoSuchFieldException, IllegalAccessException {
    setFinalField(gson, "generateNonExecutableJson", false);
    setFinalField(gson, "prettyPrinting", false);
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "lenient", false);
    setFinalField(gson, "serializeNulls", false);

    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    verify(writer, never()).write(anyString());

    assertEquals("", getJsonWriterIndent(jsonWriter));
    assertTrue(getJsonWriterHtmlSafe(jsonWriter));
    assertFalse(getJsonWriterLenient(jsonWriter));
    assertFalse(getJsonWriterSerializeNulls(jsonWriter));
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }

  private static String getJsonWriterIndent(JsonWriter jsonWriter) throws NoSuchFieldException, IllegalAccessException {
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    return (String) indentField.get(jsonWriter);
  }

  private static boolean getJsonWriterHtmlSafe(JsonWriter jsonWriter) throws NoSuchFieldException, IllegalAccessException {
    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    return (boolean) htmlSafeField.get(jsonWriter);
  }

  private static boolean getJsonWriterLenient(JsonWriter jsonWriter) throws NoSuchFieldException, IllegalAccessException {
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    return (boolean) lenientField.get(jsonWriter);
  }

  private static boolean getJsonWriterSerializeNulls(JsonWriter jsonWriter) throws NoSuchFieldException, IllegalAccessException {
    Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    return (boolean) serializeNullsField.get(jsonWriter);
  }
}