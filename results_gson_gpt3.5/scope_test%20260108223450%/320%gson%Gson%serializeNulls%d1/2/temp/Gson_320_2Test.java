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
import org.mockito.Mockito;
import java.lang.reflect.Field;

public class Gson_320_2Test {

  @Test
    @Timeout(8000)
  public void testSerializeNullsDefaultFalse() throws Exception {
    Gson gson = new Gson();

    // Access private final field 'serializeNulls' via reflection
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);

    // By default, should be false
    boolean defaultValue = serializeNullsField.getBoolean(gson);
    assertFalse(defaultValue);

    // Call serializeNulls() method and verify it returns the field value
    boolean result = gson.serializeNulls();
    assertEquals(defaultValue, result);
  }

  @Test
    @Timeout(8000)
  public void testSerializeNullsTrue() throws Exception {
    Gson gson = new Gson();

    // Use reflection to set private final field serializeNulls to true
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(gson, true);

    // Call serializeNulls() method and verify it returns true
    boolean result = gson.serializeNulls();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testSerializeNullsFalse() throws Exception {
    Gson gson = new Gson();

    // Use reflection to set private final field serializeNulls to false explicitly
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(gson, false);

    // Call serializeNulls() method and verify it returns false
    boolean result = gson.serializeNulls();
    assertFalse(result);
  }
}