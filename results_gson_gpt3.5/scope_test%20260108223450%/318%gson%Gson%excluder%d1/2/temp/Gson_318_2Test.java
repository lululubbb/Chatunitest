package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
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
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Gson_ExcluderTest {

  private Gson gson;
  private Excluder mockExcluder;

  @BeforeEach
  public void setUp() throws Exception {
    gson = new Gson();

    // Use reflection to inject a mock Excluder instance into the private final field 'excluder'
    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);

    // Remove final modifier from the field (Java 12+ may not allow this, but assuming pre-Java 12)
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(excluderField, excluderField.getModifiers() & ~Modifier.FINAL);

    mockExcluder = mock(Excluder.class);
    excluderField.set(gson, mockExcluder);
  }

  @Test
    @Timeout(8000)
  public void testExcluder_returnsInjectedInstance() throws Exception {
    // Use reflection to get the private 'excluder' field value
    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder expectedExcluder = (Excluder) excluderField.get(gson);

    Excluder actualExcluder = gson.excluder();

    assertNotNull(actualExcluder, "excluder() should not return null");
    assertSame(expectedExcluder, actualExcluder, "excluder() should return the exact Excluder instance held in the field");
  }
}