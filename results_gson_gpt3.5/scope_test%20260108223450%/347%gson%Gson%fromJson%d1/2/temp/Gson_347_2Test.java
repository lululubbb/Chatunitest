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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testFromJson_withValidJsonAndType_returnsObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Type type = MyClass.class;

    // Use reflection to invoke private fromJson(Reader, TypeToken<T>)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    MyClass result = (MyClass) fromJsonMethod.invoke(gson, reader, TypeToken.get(type));

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  void testFromJson_withNullReader_throwsJsonSyntaxException() throws Exception {
    Type type = MyClass.class;

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    assertThrows(JsonSyntaxException.class, () -> {
      try {
        fromJsonMethod.invoke(gson, (Reader) null, TypeToken.get(type));
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof JsonSyntaxException) {
          throw cause;
        }
        throw e;
      }
    });
  }

  @Test
    @Timeout(8000)
  void testFromJson_withMalformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{name:\"missing quotes}";
    Reader reader = new StringReader(malformedJson);
    Type type = MyClass.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  void testFromJson_withEmptyJson_throwsJsonSyntaxException() {
    String emptyJson = "";
    Reader reader = new StringReader(emptyJson);
    Type type = MyClass.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  void testFromJson_withNullType_throwsNullPointerException() {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);

    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, (Type) null));
  }

  // Helper class for deserialization
  static class MyClass {
    String name;
  }
}