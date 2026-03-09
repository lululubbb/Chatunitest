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

import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_341_5Test {

  private Gson gson;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();
    // Using reflection to set final lenient field since it's final
    java.lang.reflect.Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(gson, false);
  }

  @Test
    @Timeout(8000)
  void newJsonReader_shouldReturnJsonReaderWithLenientFalse() {
    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonReader jsonReader = gson.newJsonReader(reader);
    assertNotNull(jsonReader);
    assertSame(reader, getReaderFromJsonReader(jsonReader));
    assertFalse(jsonReader.isLenient());
  }

  @Test
    @Timeout(8000)
  void newJsonReader_shouldReturnJsonReaderWithLenientTrue() throws Exception {
    // Create Gson instance with lenient = true by reflection
    Gson gsonLenient = new Gson();
    java.lang.reflect.Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(gsonLenient, true);

    Reader reader = new StringReader("{\"key\":\"value\"}");
    JsonReader jsonReader = gsonLenient.newJsonReader(reader);
    assertNotNull(jsonReader);
    assertSame(reader, getReaderFromJsonReader(jsonReader));
    assertTrue(jsonReader.isLenient());
  }

  private Reader getReaderFromJsonReader(JsonReader jsonReader) {
    try {
      java.lang.reflect.Field inField = JsonReader.class.getDeclaredField("in");
      inField.setAccessible(true);
      return (Reader) inField.get(jsonReader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}