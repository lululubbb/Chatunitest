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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

class Gson_newJsonWriterTest {

  private Gson gson;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    stringWriter = new StringWriter();
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_DefaultSettings_WritesNothingAndSetsDefaults() throws IOException, NoSuchFieldException, IllegalAccessException {
    // By default, generateNonExecutableJson is false, so no prefix should be written
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // The returned JsonWriter should wrap the same writer
    assertNotNull(jsonWriter);

    // Using reflection to verify private fields of Gson
    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gson);
    assertFalse(generateNonExecutableJson);

    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = prettyPrintingField.getBoolean(gson);
    assertFalse(prettyPrinting);

    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafe = htmlSafeField.getBoolean(gson);
    assertTrue(htmlSafe);

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(gson);
    assertFalse(lenient);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gson);
    assertFalse(serializeNulls);

    // Check that no prefix was written
    assertEquals("", stringWriter.toString());

    // Verify JsonWriter configuration via reflection on JsonWriter
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    String indent = (String) indentField.get(jsonWriter);
    assertEquals("", indent);

    Field htmlSafeFieldJW = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeFieldJW.setAccessible(true);
    boolean htmlSafeJW = htmlSafeFieldJW.getBoolean(jsonWriter);
    assertEquals(htmlSafe, htmlSafeJW);

    Field lenientFieldJW = JsonWriter.class.getDeclaredField("lenient");
    lenientFieldJW.setAccessible(true);
    boolean lenientJW = lenientFieldJW.getBoolean(jsonWriter);
    assertEquals(lenient, lenientJW);

    Field serializeNullsFieldJW = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsFieldJW.setAccessible(true);
    boolean serializeNullsJW = serializeNullsFieldJW.getBoolean(jsonWriter);
    assertEquals(serializeNulls, serializeNullsJW);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_GenerateNonExecutableJson_WritesPrefix() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Create a new Gson instance with generateNonExecutableJson true by reflection on constructor fields
    // Since the default constructor does not allow setting this, we create a new instance via reflection

    // Use reflection to get the constructor with all params
    // But since the constructor is package-private, create a new Gson instance with generateNonExecutableJson true by subclassing or reflection.

    // Instead, set the field generateNonExecutableJson to true via reflection on existing gson instance
    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    generateNonExecutableJsonField.setBoolean(gson, true);

    // The prefix constant
    Field prefixField = Gson.class.getDeclaredField("JSON_NON_EXECUTABLE_PREFIX");
    prefixField.setAccessible(true);
    String prefix = (String) prefixField.get(null);

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // The prefix should be written to the writer first
    assertTrue(stringWriter.toString().startsWith(prefix));
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_PrettyPrinting_SetsIndent() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Set prettyPrinting to true via reflection
    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    prettyPrintingField.setBoolean(gson, true);

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // Check that indent is set to two spaces
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    String indent = (String) indentField.get(jsonWriter);
    assertEquals("  ", indent);
  }

  @Test
    @Timeout(8000)
  void newJsonWriter_HtmlSafeAndLenientAndSerializeNulls_PropertiesSet() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Set htmlSafe, lenient, serializeNulls to custom values
    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    htmlSafeField.setBoolean(gson, false);

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(gson, true);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(gson, true);

    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    Field htmlSafeFieldJW = JsonWriter.class.getDeclaredField("htmlSafe");
    htmlSafeFieldJW.setAccessible(true);
    boolean htmlSafeJW = htmlSafeFieldJW.getBoolean(jsonWriter);
    assertFalse(htmlSafeJW);

    Field lenientFieldJW = JsonWriter.class.getDeclaredField("lenient");
    lenientFieldJW.setAccessible(true);
    boolean lenientJW = lenientFieldJW.getBoolean(jsonWriter);
    assertTrue(lenientJW);

    Field serializeNullsFieldJW = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsFieldJW.setAccessible(true);
    boolean serializeNullsJW = serializeNullsFieldJW.getBoolean(jsonWriter);
    assertTrue(serializeNullsJW);
  }
}