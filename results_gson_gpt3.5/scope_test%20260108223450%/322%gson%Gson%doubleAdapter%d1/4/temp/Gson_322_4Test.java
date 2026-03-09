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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Method;

class GsonDoubleAdapterTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_returnsTypeAdaptersDouble_whenSerializeSpecialFloatingPointValuesTrue() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, true);

    assertSame(TypeAdapters.DOUBLE, adapter);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_adapterRead_returnsNullOnJsonNull() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    Double result = (Double) adapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_adapterRead_returnsNextDouble() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
    when(jsonReader.nextDouble()).thenReturn(123.456);

    Double result = (Double) adapter.read(jsonReader);

    verify(jsonReader, never()).nextNull();
    verify(jsonReader).nextDouble();
    assertEquals(123.456, result);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_adapterWrite_writesNullValueWhenValueIsNull() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter jsonWriter = mock(JsonWriter.class);

    adapter.write(jsonWriter, null);

    verify(jsonWriter).nullValue();
    verify(jsonWriter, never()).value(anyDouble());
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_adapterWrite_writesDoubleValueWhenValueIsNotNull() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter jsonWriter = mock(JsonWriter.class);

    Number value = 42.42;

    adapter.write(jsonWriter, value);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).value(value.doubleValue());
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_adapterWrite_throwsOnInvalidFloatingPoint() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter jsonWriter = mock(JsonWriter.class);

    Number nanValue = Double.NaN;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(jsonWriter, nanValue));
    assertTrue(thrown.getMessage().contains("NaN"));
  }
}