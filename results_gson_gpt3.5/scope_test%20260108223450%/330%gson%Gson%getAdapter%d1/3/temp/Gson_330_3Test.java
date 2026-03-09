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

class Gson_getAdapter_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void getAdapter_withClass_shouldReturnSameAdapterAsWithTypeToken() {
    Class<String> clazz = String.class;

    TypeAdapter<String> adapterFromClass = gson.getAdapter(clazz);
    TypeAdapter<String> adapterFromTypeToken = gson.getAdapter(TypeToken.get(clazz));

    assertNotNull(adapterFromClass);
    assertNotNull(adapterFromTypeToken);
    assertEquals(adapterFromTypeToken.getClass(), adapterFromClass.getClass());
  }

  @Test
    @Timeout(8000)
  void getAdapter_withPrimitiveClass_shouldReturnAdapter() {
    TypeAdapter<Integer> adapter = gson.getAdapter(int.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withCustomClass_shouldReturnAdapter() {
    class CustomClass {}
    TypeAdapter<CustomClass> adapter = gson.getAdapter(CustomClass.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withArrayClass_shouldReturnAdapter() {
    TypeAdapter<int[]> adapter = gson.getAdapter(int[].class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_withNullClass_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> gson.getAdapter((Class<?>) null));
  }
}