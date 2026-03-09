package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_439_5Test {

  private JsonParser jsonParser;

  @BeforeEach
  public void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  public void testParse_withValidJson_callsParseString() throws Throwable {
    // Arrange
    String json = "{\"key\":\"value\"}";
    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expectedElement = mock(JsonElement.class);
      mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(expectedElement);

      // Act
      JsonElement result = jsonParser.parse(json);

      // Assert
      assertSame(expectedElement, result);
      mockedStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_withNullString_throwsException() {
    String json = null;
    // parseString should throw NullPointerException or JsonSyntaxException
    assertThrows(NullPointerException.class, () -> jsonParser.parse(json));
  }

  @Test
    @Timeout(8000)
  public void testParse_withEmptyString_callsParseString() throws Throwable {
    String json = "";
    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      JsonElement expectedElement = mock(JsonElement.class);
      mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(expectedElement);

      JsonElement result = jsonParser.parse(json);

      assertSame(expectedElement, result);
      mockedStatic.verify(() -> JsonParser.parseString(json));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseString_withInvalidJson_throwsJsonSyntaxException() {
    String invalidJson = "{invalid json}";
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(invalidJson));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withValidReader_returnsJsonElement() throws IOException {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement element = JsonParser.parseReader(reader);

    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withMalformedJson_throwsJsonSyntaxException() {
    String invalidJson = "{invalid json}";
    Reader reader = new StringReader(invalidJson);

    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withNullReader_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((Reader) null));
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withJsonReader_invokesStreamsParse() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    try (MockedStatic<Streams> mockedStreams = Mockito.mockStatic(Streams.class)) {
      JsonElement expectedElement = mock(JsonElement.class);
      mockedStreams.when(() -> Streams.parse(jsonReader)).thenReturn(expectedElement);

      JsonElement result = JsonParser.parseReader(jsonReader);

      assertSame(expectedElement, result);
      mockedStreams.verify(() -> Streams.parse(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withJsonReader_throwsJsonSyntaxException_onMalformedJson() {
    JsonReader jsonReader = mock(JsonReader.class);
    try (MockedStatic<Streams> mockedStreams = Mockito.mockStatic(Streams.class)) {
      // Throw MalformedJsonException wrapped in RuntimeException to simulate Streams.parse behavior
      mockedStreams.when(() -> Streams.parse(jsonReader))
          .thenAnswer(invocation -> {
            throw new RuntimeException(new MalformedJsonException("malformed"));
          });

      RuntimeException thrown = assertThrows(RuntimeException.class, () -> JsonParser.parseReader(jsonReader));
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof JsonSyntaxException || cause instanceof MalformedJsonException,
          "Expected cause to be JsonSyntaxException or MalformedJsonException but was " + cause);
    }
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withJsonReader_throwsJsonSyntaxException_onIOException() {
    JsonReader jsonReader = mock(JsonReader.class);
    try (MockedStatic<Streams> mockedStreams = Mockito.mockStatic(Streams.class)) {
      // Throw IOException wrapped in RuntimeException to simulate Streams.parse behavior
      mockedStreams.when(() -> Streams.parse(jsonReader))
          .thenAnswer(invocation -> {
            throw new RuntimeException(new IOException("io error"));
          });

      RuntimeException thrown = assertThrows(RuntimeException.class, () -> JsonParser.parseReader(jsonReader));
      Throwable cause = thrown.getCause();
      assertTrue(cause instanceof JsonSyntaxException || cause instanceof IOException,
          "Expected cause to be JsonSyntaxException or IOException but was " + cause);
    }
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseReader_invokesParseReader() throws Throwable {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);
    JsonParser parser = new JsonParser();

    JsonElement expectedElement = mock(JsonElement.class);
    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(reader)).thenReturn(expectedElement);

      Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
      parseMethod.setAccessible(true);
      JsonElement result = (JsonElement) parseMethod.invoke(parser, reader);

      assertSame(expectedElement, result);
      mockedStatic.verify(() -> JsonParser.parseReader(reader));
    }
  }

  @Test
    @Timeout(8000)
  public void testDeprecatedParseJsonReader_invokesParseReader() throws Throwable {
    JsonReader jsonReader = mock(JsonReader.class);
    JsonParser parser = new JsonParser();

    JsonElement expectedElement = mock(JsonElement.class);
    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(jsonReader)).thenReturn(expectedElement);

      Method parseMethod = JsonParser.class.getDeclaredMethod("parse", JsonReader.class);
      parseMethod.setAccessible(true);
      JsonElement result = (JsonElement) parseMethod.invoke(parser, jsonReader);

      assertSame(expectedElement, result);
      mockedStatic.verify(() -> JsonParser.parseReader(jsonReader));
    }
  }
}