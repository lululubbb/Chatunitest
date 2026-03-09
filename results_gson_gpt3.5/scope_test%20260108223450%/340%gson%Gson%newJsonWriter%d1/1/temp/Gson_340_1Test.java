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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

class GsonNewJsonWriterTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson(
        null, // Excluder excluder
        null, // FieldNamingStrategy fieldNamingStrategy
        null, // Map<Type, InstanceCreator<?>> instanceCreators
        false, // serializeNulls
        false, // complexMapKeySerialization
        false, // generateNonExecutableGson
        true,  // htmlSafe
        false, // prettyPrinting
        false, // lenient
        false, // serializeSpecialFloatingPointValues
        true,  // useJdkUnsafe
        null,  // LongSerializationPolicy longSerializationPolicy
        null,  // String datePattern
        0,     // int dateStyle
        0,     // int timeStyle
        null,  // List<TypeAdapterFactory> builderFactories
        null,  // List<TypeAdapterFactory> builderHierarchyFactories
        null,  // List<TypeAdapterFactory> factoriesToBeAdded
        null,  // ToNumberStrategy objectToNumberStrategy
        null,  // ToNumberStrategy numberToNumberStrategy
        null   // List<ReflectionAccessFilter> reflectionFilters
    );
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_generateNonExecutableJsonFalse_prettyPrintingFalse_setsPropertiesCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);
    // The writer should NOT contain the prefix
    assertFalse(writer.toString().startsWith(")]}'\n"));

    // Using reflection to verify private fields of JsonWriter
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    String indentValue = (String) indentField.get(jsonWriter);
    assertEquals("", indentValue);

    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafeValue = (boolean) htmlSafeField.get(jsonWriter);
    assertTrue(htmlSafeValue);

    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenientValue = (boolean) lenientField.get(jsonWriter);
    assertFalse(lenientValue);

    Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNullsValue = (boolean) serializeNullsField.get(jsonWriter);
    assertFalse(serializeNullsValue);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_generateNonExecutableJsonTrue_writesPrefix() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Use reflection to set private final field generateNonExecutableJson to true
    Field field = Gson.class.getDeclaredField("generateNonExecutableJson");
    field.setAccessible(true);
    field.set(gson, true);

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);
    // The writer should start with the prefix
    assertTrue(writer.toString().startsWith(")]}'\n"));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_prettyPrintingTrue_setsIndent() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Set prettyPrinting to true via reflection
    Field field = Gson.class.getDeclaredField("prettyPrinting");
    field.setAccessible(true);
    field.set(gson, true);

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);

    // Verify indent is set to two spaces
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    String indentValue = (String) indentField.get(jsonWriter);
    assertEquals("  ", indentValue);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_htmlSafeFalse_setsHtmlSafeFalse() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Set htmlSafe to false via reflection
    Field field = Gson.class.getDeclaredField("htmlSafe");
    field.setAccessible(true);
    field.set(gson, false);

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);

    Field htmlSafeField = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafeValue = (boolean) htmlSafeField.get(jsonWriter);
    assertFalse(htmlSafeValue);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_lenientTrue_setsLenientTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Set lenient to true via reflection
    Field field = Gson.class.getDeclaredField("lenient");
    field.setAccessible(true);
    field.set(gson, true);

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);

    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenientValue = (boolean) lenientField.get(jsonWriter);
    assertTrue(lenientValue);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_serializeNullsTrue_setsSerializeNullsTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    Writer writer = new StringWriter();

    // Set serializeNulls to true via reflection
    Field field = Gson.class.getDeclaredField("serializeNulls");
    field.setAccessible(true);
    field.set(gson, true);

    // Act
    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    // Assert
    assertNotNull(jsonWriter);

    Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNullsValue = (boolean) serializeNullsField.get(jsonWriter);
    assertTrue(serializeNullsValue);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_writerThrowsIOException_propagatesException() throws IOException {
    // Arrange
    Writer writer = mock(Writer.class);
    doThrow(new IOException("write failed")).when(writer).write(anyString());

    // Set generateNonExecutableJson to true to trigger write call
    try {
      Field field = Gson.class.getDeclaredField("generateNonExecutableJson");
      field.setAccessible(true);
      field.set(gson, true);
    } catch (Exception e) {
      fail("Reflection setup failed");
    }

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> gson.newJsonWriter(writer));
    assertEquals("write failed", thrown.getMessage());
  }
}