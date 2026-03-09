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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_validJson_returnsObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;

    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class)) {
      TypeToken<Dummy> dummyTypeToken = TypeToken.get(clazz);
      mockedTypeToken.when(() -> TypeToken.get(clazz)).thenReturn(dummyTypeToken);

      // Spy on Gson to mock fromJson(Reader, TypeToken<T>)
      Gson spyGson = Mockito.spy(gson);
      Dummy dummyInstance = new Dummy("test");
      doReturn(dummyInstance).when(spyGson).fromJson(reader, dummyTypeToken);

      Dummy result = spyGson.fromJson(reader, clazz);

      assertNotNull(result);
      assertEquals("test", result.name);
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_nullReader_returnsNull() throws Exception {
    Reader reader = null;
    Class<Dummy> clazz = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class)) {
      TypeToken<Dummy> dummyTypeToken = TypeToken.get(clazz);
      mockedTypeToken.when(() -> TypeToken.get(clazz)).thenReturn(dummyTypeToken);
      doReturn(null).when(spyGson).fromJson(reader, dummyTypeToken);

      Dummy result = spyGson.fromJson(reader, clazz);

      assertNull(result);
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_castThrowsClassCastException_throwsJsonSyntaxException() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Class<Dummy> clazz = Dummy.class;

    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class);
        MockedStatic<Primitives> mockedPrimitives = Mockito.mockStatic(Primitives.class)) {
      TypeToken<Dummy> dummyTypeToken = TypeToken.get(clazz);
      mockedTypeToken.when(() -> TypeToken.get(clazz)).thenReturn(dummyTypeToken);

      Gson spyGson = Mockito.spy(gson);
      Object object = new Object(); // Not Dummy
      doReturn(object).when(spyGson).fromJson(reader, dummyTypeToken);

      mockedPrimitives.when(() -> Primitives.wrap(clazz)).thenReturn((Class) clazz);

      assertThrows(ClassCastException.class, () -> {
        spyGson.fromJson(reader, clazz);
      });
    }
  }

  // Dummy class for testing
  static class Dummy {
    String name;

    Dummy(String name) {
      this.name = name;
    }
  }
}