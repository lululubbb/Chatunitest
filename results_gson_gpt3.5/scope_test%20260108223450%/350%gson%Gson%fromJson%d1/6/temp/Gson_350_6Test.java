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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
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
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {
  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonReaderAndType_returnsExpectedObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader stringReader = new StringReader(json);
    JsonReader jsonReader = new JsonReader(stringReader);

    // Use reflection to invoke private fromJson(JsonReader, TypeToken)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    @SuppressWarnings("unchecked")
    MyClass result = (MyClass) fromJsonMethod.invoke(gson, jsonReader, typeToken);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJsonReader_throwsNullPointerException() {
    Type type = MyClass.class;
    assertThrows(NullPointerException.class, () -> gson.fromJson((JsonReader) null, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withMalformedJson_throwsJsonSyntaxException() throws IOException {
    String malformedJson = "{name:\"test\""; // missing closing brace
    JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));

    Type type = MyClass.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withEmptyJson_returnsNull() throws Exception {
    String emptyJson = "";
    JsonReader jsonReader = new JsonReader(new StringReader(emptyJson));

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    assertThrows(JsonSyntaxException.class, () -> {
      fromJsonMethod.invoke(gson, jsonReader, typeToken);
    });
  }

  @Test
    @Timeout(8000)
  public void fromJson_withJsonNonExecutablePrefix_skipsPrefixAndParses() throws Exception {
    String jsonWithPrefix = ")]}'\n{\"name\":\"test\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonWithPrefix));

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

    MyClass result = (MyClass) fromJsonMethod.invoke(gson, jsonReader, typeToken);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  static class MyClass {
    String name;
  }
}