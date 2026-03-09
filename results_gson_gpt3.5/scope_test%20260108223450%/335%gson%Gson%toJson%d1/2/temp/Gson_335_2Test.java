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
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_withNonNullObject_callsToJsonWithClass() throws IOException {
    Appendable writer = mock(Appendable.class);
    Object src = "testString";

    gson.toJson(src, writer);

    // No exception means success, but also test with reflection the private method call indirectly
    // Use reflection to get toJson(Object, Type, Appendable) to verify it exists and is callable
    try {
      Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, java.lang.reflect.Type.class, Appendable.class);
      toJsonMethod.setAccessible(true);
      toJsonMethod.invoke(gson, src, src.getClass(), writer);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("toJson(Object, Type, Appendable) method should exist and be accessible");
    }
  }

  @Test
    @Timeout(8000)
  void toJson_withNullObject_callsToJsonWithJsonNullInstance() throws IOException {
    Appendable writer = mock(Appendable.class);

    gson.toJson(null, writer);

    // Use reflection to verify that toJson(JsonNull.INSTANCE, Appendable) can be called
    try {
      Class<?> jsonNullClass = Class.forName("com.google.gson.JsonNull");
      Object jsonNullInstance = jsonNullClass.getField("INSTANCE").get(null);

      Method toJsonWithTypeMethod = Gson.class.getDeclaredMethod("toJson", Object.class, java.lang.reflect.Type.class, Appendable.class);
      toJsonWithTypeMethod.setAccessible(true);
      toJsonWithTypeMethod.invoke(gson, jsonNullInstance, Object.class, writer);
    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      fail("Reflection invocation failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void toJson_withAppendableThrowingIOException_throwsJsonIOException() {
    Appendable writer = mock(Appendable.class);
    Object src = "test";

    try {
      doThrow(new IOException("IO error")).when(writer).append(anyChar());
    } catch (IOException e) {
      fail("Mock setup failed");
    }

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(src, writer));
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("IO error", thrown.getCause().getMessage());
  }
}