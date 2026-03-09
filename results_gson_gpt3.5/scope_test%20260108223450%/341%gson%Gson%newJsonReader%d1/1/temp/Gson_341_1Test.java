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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

public class Gson_341_1Test {

  private Gson gson;

  @BeforeEach
  public void setUp() throws Exception {
    gson = new Gson();

    // Set lenient field to true using reflection to test both branches
    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(gson, true);
  }

  @Test
    @Timeout(8000)
  public void testNewJsonReader_withLenientTrue() {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonReader jsonReader = gson.newJsonReader(reader);

    assertNotNull(jsonReader);
    assertEquals(true, getLenientValue(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testNewJsonReader_withLenientFalse() throws Exception {
    // Set lenient to false
    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(gson, false);

    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonReader jsonReader = gson.newJsonReader(reader);

    assertNotNull(jsonReader);
    assertEquals(false, getLenientValue(jsonReader));
  }

  // Helper method to get the lenient value from JsonReader via reflection
  private boolean getLenientValue(JsonReader jsonReader) {
    try {
      Field lenientField = JsonReader.class.getDeclaredField("lenient");
      lenientField.setAccessible(true);
      return lenientField.getBoolean(jsonReader);
    } catch (Exception e) {
      fail("Reflection failed to get lenient field from JsonReader: " + e.getMessage());
      return false;
    }
  }
}