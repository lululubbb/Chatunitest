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

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_shouldReturnObject() throws Exception {
    String json = "{\"value\":\"test\"}";
    Reader reader = new StringReader(json);
    Class<FakeClass> clazz = FakeClass.class;

    // Spy on gson to mock fromJson(Reader, TypeToken)
    Gson spyGson = Mockito.spy(gson);
    FakeClass expected = new FakeClass("test");

    // Mock return of fromJson(Reader, TypeToken)
    doReturn(expected).when(spyGson).fromJson(any(Reader.class), any(TypeToken.class));

    FakeClass actual = spyGson.fromJson(reader, clazz);

    assertNotNull(actual);
    assertEquals(expected.value, actual.value);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullReader_shouldThrowNullPointerException() {
    Class<FakeClass> clazz = FakeClass.class;
    assertThrows(NullPointerException.class, () -> gson.fromJson((Reader) null, clazz));
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullClass_shouldThrowNullPointerException() {
    Reader reader = new StringReader("{}");
    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, (Class<?>) null));
  }

  @Test
    @Timeout(8000)
  public void fromJson_PrimitivesWrapCalled_shouldCastCorrectly() throws Exception {
    String json = "123";
    Reader reader = new StringReader(json);
    Class<Integer> clazz = Integer.class;

    Gson spyGson = Mockito.spy(gson);
    Integer expected = 123;

    doReturn(expected).when(spyGson).fromJson(any(Reader.class), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMock = Mockito.mockStatic(Primitives.class)) {
      primitivesMock.when(() -> Primitives.wrap(clazz)).thenReturn(clazz);

      Integer actual = spyGson.fromJson(reader, clazz);

      primitivesMock.verify(() -> Primitives.wrap(clazz));
      assertEquals(expected, actual);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_reflectionInvokePrivateFromJson() throws Exception {
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, Class.class);
    fromJsonMethod.setAccessible(true);

    String json = "{\"value\":\"test\"}";
    Reader reader = new StringReader(json);
    Class<FakeClass> clazz = FakeClass.class;

    // Spy to mock internal fromJson(Reader, TypeToken)
    Gson spyGson = Mockito.spy(gson);
    FakeClass expected = new FakeClass("test");
    doReturn(expected).when(spyGson).fromJson(any(Reader.class), any(TypeToken.class));

    Object result = fromJsonMethod.invoke(spyGson, reader, clazz);

    assertNotNull(result);
    assertTrue(result instanceof FakeClass);
    assertEquals("test", ((FakeClass) result).value);
  }

  static class FakeClass {
    String value;

    public FakeClass() {}

    public FakeClass(String value) {
      this.value = value;
    }
  }
}