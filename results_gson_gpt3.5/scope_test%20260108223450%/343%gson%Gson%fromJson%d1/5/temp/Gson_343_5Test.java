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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJson_StringClass_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonAndClass_returnsExpectedObject() {
    String json = "{\"value\":42}";
    Class<DummyClass> clazz = DummyClass.class;

    DummyClass result = gson.fromJson(json, clazz);

    assertNotNull(result);
    assertEquals(42, result.value);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJson_returnsNull() {
    String json = null;
    Class<DummyClass> clazz = DummyClass.class;

    DummyClass result = gson.fromJson(json, clazz);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullClass_throwsNullPointerException() {
    String json = "{\"value\":42}";

    assertThrows(NullPointerException.class, () -> {
      // Cast null to Class<?> to disambiguate method call
      gson.fromJson(json, (Class<?>) null);
    });
  }

  @Test
    @Timeout(8000)
  public void fromJson_callsPrimitivesWrapAndCast() throws Exception {
    String json = "{\"value\":42}";
    Class<DummyClass> clazz = DummyClass.class;

    try (MockedStatic<Primitives> mockedPrimitives = Mockito.mockStatic(Primitives.class)) {
      DummyClass dummyObject = new DummyClass();
      dummyObject.value = 42;

      // Spy the Gson instance
      Gson spyGson = spy(gson);

      // Mock fromJson(String, TypeToken) to return dummyObject
      doReturn(dummyObject).when(spyGson).fromJson(eq(json), any(TypeToken.class));

      // Mock Primitives.wrap to return clazz itself (simulate wrapper)
      mockedPrimitives.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      DummyClass result = spyGson.fromJson(json, clazz);

      assertNotNull(result);
      assertEquals(42, result.value);

      mockedPrimitives.verify(() -> Primitives.wrap(clazz));
    }
  }

  static class DummyClass {
    int value;
  }
}