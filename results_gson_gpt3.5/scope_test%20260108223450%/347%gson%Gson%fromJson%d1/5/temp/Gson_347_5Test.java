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
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonAndType_returnsObject() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);
    Type type = java.util.Map.class;

    // Using reflection to invoke public fromJson(Reader, TypeToken<T>)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Object result = fromJsonMethod.invoke(gson, reader, TypeToken.get(type));

    assertNotNull(result);
    assertTrue(result instanceof java.util.Map);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullReader_throwsNullPointerException() {
    Reader reader = null;
    Type type = String.class;

    assertThrows(NullPointerException.class, () -> {
      gson.fromJson(reader, type);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withInvalidJson_throwsJsonSyntaxException() {
    String invalidJson = "{invalid json}";
    Reader reader = new StringReader(invalidJson);
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, type);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withEmptyJson_throwsJsonSyntaxException() {
    String emptyJson = "";
    Reader reader = new StringReader(emptyJson);
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, type);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withTypeTokenNull_throwsException() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      fromJsonMethod.invoke(gson, reader, (Object) null);
    });
    // Optionally check cause or message if needed
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withTypeToken_getsAdapterAndParses() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);
    @SuppressWarnings("unchecked")
    TypeToken<Map<String, String>> typeToken = (TypeToken<Map<String, String>>) TypeToken.getParameterized(Map.class, String.class, String.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Object result = fromJsonMethod.invoke(gson, reader, typeToken);

    assertNotNull(result);
    assertTrue(result instanceof Map);
  }
}