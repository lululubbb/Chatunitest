package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class NumberTypeAdapter_300_3Test {

  private ToNumberStrategy toNumberStrategy;
  private NumberTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    toNumberStrategy = mock(ToNumberStrategy.class);
    Constructor<NumberTypeAdapter> ctor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    ctor.setAccessible(true);
    adapter = ctor.newInstance(toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Number result = adapter.read(in);

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_validNumber() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    Number expectedNumber = 42;
    when(toNumberStrategy.readNumber(in)).thenReturn(expectedNumber);

    Number result = adapter.read(in);

    verify(in).peek();
    verify(toNumberStrategy).readNumber(in);
    assertEquals(expectedNumber, result);
  }

  @Test
    @Timeout(8000)
  void testRead_invalidToken_throws() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(ex.getMessage().contains("Expecting number, got: BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).value((Number) null);
  }

  @Test
    @Timeout(8000)
  void testWrite_validNumber() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Number value = 123;

    adapter.write(out, value);

    verify(out).value(value);
  }

  @Test
    @Timeout(8000)
  void testGetFactory_returnsNonNullFactory() throws Exception {
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    var factory = NumberTypeAdapter.getFactory(strategy);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void testNewFactory_returnsNonNullFactory() throws Exception {
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object factory = newFactoryMethod.invoke(null, ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
  }
}