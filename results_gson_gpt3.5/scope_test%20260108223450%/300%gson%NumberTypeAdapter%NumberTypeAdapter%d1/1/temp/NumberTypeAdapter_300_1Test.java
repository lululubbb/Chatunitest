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
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class NumberTypeAdapter_300_1Test {

  private ToNumberStrategy toNumberStrategy;
  private NumberTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    toNumberStrategy = mock(ToNumberStrategy.class);
    // Use reflection to access private constructor
    Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Number result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNull_success() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    Number expectedNumber = 123;
    when(toNumberStrategy.readNumber(in)).thenReturn(expectedNumber);

    Number result = adapter.read(in);

    assertEquals(expectedNumber, result);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNull_throwsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(toNumberStrategy.readNumber(in)).thenThrow(new JsonSyntaxException("error"));

    assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).value((String) isNull());
  }

  @Test
    @Timeout(8000)
  void testWrite_nonNull() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Number value = 42;

    adapter.write(out, value);

    verify(out).value(value);
  }

  @Test
    @Timeout(8000)
  void testNewFactory_andGetFactory() throws Exception {
    // Use reflection to invoke private static newFactory method
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object factoryFromNewFactory = newFactoryMethod.invoke(null, ToNumberPolicy.LAZILY_PARSED_NUMBER);

    assertNotNull(factoryFromNewFactory);

    // Call public static getFactory method
    var factoryFromGetFactory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factoryFromGetFactory);
  }
}