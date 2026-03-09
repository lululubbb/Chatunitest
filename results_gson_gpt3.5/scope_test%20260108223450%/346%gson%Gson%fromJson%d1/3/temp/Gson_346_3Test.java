package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
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
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJson_returnsObject() throws Exception {
    String json = "{\"field\":\"value\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;
    Dummy dummy = new Dummy();

    // Spy on Gson instance
    Gson spyGson = Mockito.spy(gson);
    TypeToken<Dummy> typeToken = TypeToken.get(clazz);

    // Mock fromJson(Reader, TypeToken) to return dummy
    doReturn(dummy).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    // Mock Primitives.wrap(clazz) to return clazz itself
    try (MockedStatic<Primitives> primitivesStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesStatic.when(() -> Primitives.wrap(clazz)).thenReturn((Class) clazz);

      // Call the real method fromJson(Reader, Class) on spyGson
      Dummy result = spyGson.fromJson(reader, clazz);
      assertSame(dummy, result);
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullReader_throwsJsonSyntaxException() {
    Class<Dummy> clazz = Dummy.class;
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson((Reader) null, clazz));
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullClass_throwsNullPointerException() throws Throwable {
    Reader reader = new StringReader("{}");
    var fromJsonMethod = Gson.class.getMethod("fromJson", Reader.class, Class.class);
    assertThrows(NullPointerException.class, () -> {
      try {
        fromJsonMethod.invoke(gson, reader, (Object) null);
      } catch (Exception e) {
        throw e.getCause();
      }
    });
  }

  // Dummy class for testing
  static class Dummy {
    String field;
  }
}