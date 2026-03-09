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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_returnsObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Class<DummyClass> clazz = DummyClass.class;
    DummyClass dummy = new DummyClass();

    // Spy on gson to mock fromJson(Reader, TypeToken<T>)
    Gson spyGson = Mockito.spy(gson);
    TypeToken<DummyClass> typeToken = TypeToken.get(clazz);

    // Use doAnswer to call real method for fromJson(Reader, Class) to avoid recursion
    doReturn(dummy).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

    // Mock Primitives.wrap to return the class itself (as DummyClass is not primitive)
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      DummyClass result = spyGson.fromJson(reader, clazz);

      assertNotNull(result);
      assertSame(dummy, result);

      // Verify fromJson(Reader, TypeToken<T>) was called with any Reader and typeToken
      verify(spyGson).fromJson(any(Reader.class), eq(typeToken));

      // Verify Primitives.wrap was called with classOfT
      primitivesMockedStatic.verify(() -> Primitives.wrap(clazz));
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_throwsNullPointerException() {
    Reader reader = null;
    Class<DummyClass> clazz = DummyClass.class;

    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, clazz));
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullClass_throwsNullPointerException() {
    Reader reader = new StringReader("{}");
    Class<?> clazz = null;

    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, clazz));
  }

  // Dummy class for deserialization target
  private static class DummyClass {
    String name;
  }
}