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

public class NumberTypeAdapter_300_4Test {

  private ToNumberStrategy toNumberStrategy;
  private NumberTypeAdapter adapter;

  @BeforeEach
  public void setUp() throws Exception {
    toNumberStrategy = mock(ToNumberStrategy.class);
    Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  public void testRead_null() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Number result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_number() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(toNumberStrategy.readNumber(reader)).thenReturn(123);

    Number result = adapter.read(reader);

    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_stringNumber() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(toNumberStrategy.readNumber(reader)).thenReturn(456);

    Number result = adapter.read(reader);

    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_invalidToken_throwsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BOOLEAN);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    assertTrue(ex.getMessage().contains("Expecting number, got: BOOLEAN"));
  }

  @Test
    @Timeout(8000)
  public void testWrite_null() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).value((String) isNull());
    verify(writer, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWrite_number() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Number number = 789;

    adapter.write(writer, number);

    verify(writer).value(number);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_andGetFactory() throws Exception {
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    Object factoryFromNewFactory = newFactoryMethod.invoke(null, strategy);

    Object factoryFromGetFactory = NumberTypeAdapter.getFactory(strategy);

    assertNotNull(factoryFromNewFactory);
    assertNotNull(factoryFromGetFactory);
    assertEquals(factoryFromNewFactory.getClass(), factoryFromGetFactory.getClass());
  }
}