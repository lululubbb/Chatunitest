package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumberTypeAdapter_300_5Test {

  private ToNumberStrategy toNumberStrategy;
  private NumberTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    toNumberStrategy = mock(ToNumberStrategy.class);
    Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  void testRead_NullValue() throws IOException {
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
  void testRead_ValidNumber() throws IOException {
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
  void testRead_InvalidNumber_ThrowsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    // Wrap NumberFormatException inside JsonSyntaxException to match expected exception
    when(toNumberStrategy.readNumber(in)).thenAnswer(invocation -> {
      throw new JsonSyntaxException("bad number", new NumberFormatException("bad number"));
    });

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));

    assertTrue(ex.getMessage().contains("bad number"));
    verify(in).peek();
    verify(toNumberStrategy).readNumber(in);
  }

  @Test
    @Timeout(8000)
  void testWrite_NullValue() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).value((String) isNull());
    verify(out, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_ValidNumber() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Number value = 123;

    adapter.write(out, value);

    verify(out).value(value);
  }

  @Test
    @Timeout(8000)
  void testNewFactory_andGetFactory() throws Exception {
    // Use reflection to invoke private static newFactory method
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    Object factory = newFactoryMethod.invoke(null, strategy);
    assertNotNull(factory);

    TypeAdapterFactory factoryFromGetFactory = NumberTypeAdapter.getFactory(strategy);
    assertNotNull(factoryFromGetFactory);
  }

  @Test
    @Timeout(8000)
  void testLAZILY_PARSED_NUMBER_FACTORY_notNull() throws Exception {
    // Access private static field LAZILY_PARSED_NUMBER_FACTORY
    var field = NumberTypeAdapter.class.getDeclaredField("LAZILY_PARSED_NUMBER_FACTORY");
    field.setAccessible(true);
    Object factory = field.get(null);
    assertNotNull(factory);
  }
}