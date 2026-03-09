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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_returnsNull() throws Exception {
    // Act
    String json = null;
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Using reflection to invoke fromJson(String, TypeToken<T>)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", String.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);
    Object result = fromJsonMethod.invoke(gson, json, typeToken);

    // Assert
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_returnsObject() {
    String json = "\"hello\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);

    String result = gson.fromJson(json, typeToken);

    assertEquals("hello", result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_throwsJsonSyntaxException() {
    String json = "";

    TypeToken<String> typeToken = TypeToken.get(String.class);

    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(json, typeToken);
    });
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_throwsJsonSyntaxException() {
    String json = "{ invalid json }";

    TypeToken<String> typeToken = TypeToken.get(String.class);

    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(json, typeToken);
    });
  }

  @Test
    @Timeout(8000)
  public void fromJson_jsonArray_returnsList() {
    String json = "[\"a\",\"b\",\"c\"]";

    TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};

    java.util.List<String> result = gson.fromJson(json, typeToken);

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("a", result.get(0));
    assertEquals("b", result.get(1));
    assertEquals("c", result.get(2));
  }
}