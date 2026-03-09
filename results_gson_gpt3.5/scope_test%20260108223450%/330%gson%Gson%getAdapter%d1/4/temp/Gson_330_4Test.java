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

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Gson_GetAdapterTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void getAdapter_withClass_returnsTypeAdapter() {
    TypeAdapter<String> adapter = gson.getAdapter(String.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withTypeToken_returnsTypeAdapter() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withNullClass_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gson.getAdapter((Class<?>) null));
  }

  @Test
    @Timeout(8000)
  void getAdapter_withNullTypeToken_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gson.getAdapter((TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  void getAdapter_invokesGetAdapterWithTypeToken_usingReflection() throws Exception {
    // Use reflection to invoke public getAdapter(TypeToken<T>) method
    Method method = Gson.class.getDeclaredMethod("getAdapter", TypeToken.class);
    method.setAccessible(true);

    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = (TypeAdapter<String>) method.invoke(gson, typeToken);

    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withCustomClass_returnsTypeAdapter() {
    class Custom {}

    TypeAdapter<Custom> adapter = gson.getAdapter(Custom.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withPrimitiveType_returnsTypeAdapter() {
    TypeAdapter<Integer> adapter = gson.getAdapter(int.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withArrayType_returnsTypeAdapter() {
    TypeAdapter<String[]> adapter = gson.getAdapter(String[].class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withParameterizedTypeToken_returnsTypeAdapter() {
    TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};
    TypeAdapter<java.util.List<String>> adapter = gson.getAdapter(typeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_cachesTypeAdapterInstances() {
    TypeAdapter<String> adapter1 = gson.getAdapter(String.class);
    TypeAdapter<String> adapter2 = gson.getAdapter(String.class);
    assertSame(adapter1, adapter2);
  }
}