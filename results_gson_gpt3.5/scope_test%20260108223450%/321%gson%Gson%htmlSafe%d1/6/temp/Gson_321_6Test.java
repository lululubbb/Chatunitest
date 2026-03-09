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
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class GsonHtmlSafeTest {

  @Test
    @Timeout(8000)
  void testHtmlSafeTrue() throws Exception {
    // Create Gson instance normally
    Gson gson = new Gson();

    // Use reflection to set the private final field "htmlSafe" to true
    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);

    // Remove final modifier using reflection (works in Java 8 and below)
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(htmlSafeField, htmlSafeField.getModifiers() & ~Modifier.FINAL);

    htmlSafeField.setBoolean(gson, true);

    boolean result = gson.htmlSafe();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testHtmlSafeFalse() throws Exception {
    // Create Gson instance normally
    Gson gson = new Gson();

    // Use reflection to set the private final field "htmlSafe" to false
    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);

    // Remove final modifier using reflection (works in Java 8 and below)
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(htmlSafeField, htmlSafeField.getModifiers() & ~Modifier.FINAL);

    htmlSafeField.setBoolean(gson, false);

    boolean result = gson.htmlSafe();
    assertFalse(result);
  }
}