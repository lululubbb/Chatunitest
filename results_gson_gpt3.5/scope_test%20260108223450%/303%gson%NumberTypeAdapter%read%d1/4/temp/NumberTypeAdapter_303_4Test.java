package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class NumberTypeAdapter_303_4Test {

  private ToNumberStrategy toNumberStrategyMock;
  private NumberTypeAdapter numberTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    toNumberStrategyMock = mock(ToNumberStrategy.class);
    // Use reflection to invoke private constructor
    var constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    numberTypeAdapter = (NumberTypeAdapter) constructor.newInstance(toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  void read_nullToken_returnsNull() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    when(jsonReaderMock.peek()).thenReturn(JsonToken.NULL);

    // nextNull() should be called once
    doNothing().when(jsonReaderMock).nextNull();

    Number result = numberTypeAdapter.read(jsonReaderMock);

    assertNull(result);
    InOrder inOrder = inOrder(jsonReaderMock);
    inOrder.verify(jsonReaderMock).peek();
    inOrder.verify(jsonReaderMock).nextNull();
    verifyNoMoreInteractions(jsonReaderMock);
    verifyNoInteractions(toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  void read_numberToken_delegatesToToNumberStrategy() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    when(jsonReaderMock.peek()).thenReturn(JsonToken.NUMBER);

    Number expectedNumber = 123.45;
    when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

    Number result = numberTypeAdapter.read(jsonReaderMock);

    assertSame(expectedNumber, result);
    verify(jsonReaderMock).peek();
    verify(toNumberStrategyMock).readNumber(jsonReaderMock);
    verifyNoMoreInteractions(jsonReaderMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  void read_stringToken_delegatesToToNumberStrategy() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);

    Number expectedNumber = 678.9;
    when(toNumberStrategyMock.readNumber(jsonReaderMock)).thenReturn(expectedNumber);

    Number result = numberTypeAdapter.read(jsonReaderMock);

    assertSame(expectedNumber, result);
    verify(jsonReaderMock).peek();
    verify(toNumberStrategyMock).readNumber(jsonReaderMock);
    verifyNoMoreInteractions(jsonReaderMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  void read_otherToken_throwsJsonSyntaxException() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    when(jsonReaderMock.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    when(jsonReaderMock.getPath()).thenReturn("$.some.path");

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
      numberTypeAdapter.read(jsonReaderMock);
    });

    assertEquals("Expecting number, got: BEGIN_OBJECT; at path $.some.path", exception.getMessage());
    verify(jsonReaderMock).peek();
    verify(jsonReaderMock).getPath();
    verifyNoMoreInteractions(jsonReaderMock);
    verifyNoInteractions(toNumberStrategyMock);
  }
}