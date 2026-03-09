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

public class Gson_320_1Test {

  @Test
    @Timeout(8000)
  public void testSerializeNulls_DefaultConstructor() {
    Gson gson = new Gson();
    // Default constructor sets serializeNulls to false
    assertFalse(gson.serializeNulls());
  }

  @Test
    @Timeout(8000)
  public void testSerializeNulls_TrueValue() throws Exception {
    // Use reflection to create Gson instance with serializeNulls = true
    Gson gson = createGsonWithSerializeNulls(true);
    assertTrue(gson.serializeNulls());
  }

  @Test
    @Timeout(8000)
  public void testSerializeNulls_FalseValue() throws Exception {
    // Use reflection to create Gson instance with serializeNulls = false
    Gson gson = createGsonWithSerializeNulls(false);
    assertFalse(gson.serializeNulls());
  }

  private Gson createGsonWithSerializeNulls(boolean serializeNullsValue) throws Exception {
    // Use default constructor and set serializeNulls field via reflection
    Gson gson = new Gson();
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.set(gson, serializeNullsValue);
    return gson;
  }
}