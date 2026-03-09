package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class NumberTypeAdapter_300_2Test {

  private ToNumberStrategy toNumberStrategyMock;
  private NumberTypeAdapter numberTypeAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    toNumberStrategyMock = mock(ToNumberStrategy.class);
    Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    numberTypeAdapter = constructor.newInstance(toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  public void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Number result = numberTypeAdapter.read(in);

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_number() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    Number expectedNumber = 42;
    when(toNumberStrategyMock.readNumber(in)).thenReturn(expectedNumber);

    Number result = numberTypeAdapter.read(in);

    verify(in).peek();
    verify(toNumberStrategyMock).readNumber(in);
    assertEquals(expectedNumber, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_notNumberOrNull_throws() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      numberTypeAdapter.read(in);
    });

    assertTrue(thrown.getMessage().contains("Expected a Number"));
  }

  @Test
    @Timeout(8000)
  public void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    numberTypeAdapter.write(out, null);

    verify(out).value((String) isNull());
  }

  @Test
    @Timeout(8000)
  public void testWrite_number() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Number value = 123;

    numberTypeAdapter.write(out, value);

    verify(out).value(value);
  }

  @Test
    @Timeout(8000)
  public void testGetFactory_returnsFactory() throws Exception {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    assertSame(factory, NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER));
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_reflection() throws Exception {
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    TypeAdapterFactory factory = (TypeAdapterFactory) newFactoryMethod.invoke(null, ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
  }
}