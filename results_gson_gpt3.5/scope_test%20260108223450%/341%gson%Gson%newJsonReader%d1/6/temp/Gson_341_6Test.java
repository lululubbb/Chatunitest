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
import com.google.gson.stream.JsonToken;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Gson_newJsonReader_Test {

  private Gson gson;
  private Field lenientField;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();
    // Use reflection to access the private final lenient field
    lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);

    // Remove final modifier from lenient field to allow modification
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(lenientField, lenientField.getModifiers() & ~Modifier.FINAL);
  }

  @Test
    @Timeout(8000)
  void newJsonReader_lenientFalse_setsLenientFalse() throws Exception {
    // Arrange
    Reader reader = new StringReader("{}");
    lenientField.setBoolean(gson, false);

    // Act
    JsonReader jsonReader = gson.newJsonReader(reader);

    // Assert
    assertNotNull(jsonReader);
    assertFalse(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void newJsonReader_lenientTrue_setsLenientTrue() throws Exception {
    // Arrange
    Reader reader = new StringReader("{}");
    lenientField.setBoolean(gson, true);

    // Act
    JsonReader jsonReader = gson.newJsonReader(reader);

    // Assert
    assertNotNull(jsonReader);
    assertTrue(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void newJsonReader_withMockedReader_returnsJsonReader() throws Exception {
    // Arrange
    Reader mockedReader = mock(Reader.class);
    lenientField.setBoolean(gson, false);

    // Act
    JsonReader jsonReader = gson.newJsonReader(mockedReader);

    // Assert
    assertNotNull(jsonReader);
    assertFalse(jsonReader.isLenient());
  }
}