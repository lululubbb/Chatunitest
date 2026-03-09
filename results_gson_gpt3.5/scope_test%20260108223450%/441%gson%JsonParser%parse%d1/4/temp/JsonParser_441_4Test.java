package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonParser_441_4Test {

  private JsonParser jsonParser;

  @BeforeEach
  void setUp() {
    jsonParser = new JsonParser();
  }

  @Test
    @Timeout(8000)
  void parse_callsParseReaderWithJsonReader_returnsJsonElement() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);
    JsonElement expectedElement = mock(JsonElement.class);

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(jsonReader)).thenReturn(expectedElement);

      JsonElement actual = jsonParser.parse(jsonReader);

      assertSame(expectedElement, actual);
      mockedStatic.verify(() -> JsonParser.parseReader(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonIOException_whenParseReaderThrowsIOException() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(jsonReader))
          .thenAnswer(invocation -> {
            throw new IOException("io error");
          });

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        try {
          jsonParser.parse(jsonReader);
        } catch (IOException e) {
          throw new JsonIOException(e);
        }
      });
      assertEquals("io error", thrown.getCause().getMessage());
      mockedStatic.verify(() -> JsonParser.parseReader(jsonReader));
    }
  }

  @Test
    @Timeout(8000)
  void parse_throwsJsonSyntaxException_whenParseReaderThrowsMalformedJsonException() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);

    try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
      mockedStatic.when(() -> JsonParser.parseReader(jsonReader))
          .thenAnswer(invocation -> {
            throw new MalformedJsonException("malformed");
          });

      JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
        try {
          jsonParser.parse(jsonReader);
        } catch (RuntimeException e) {
          Throwable cause = e.getCause();
          if (cause instanceof MalformedJsonException) {
            throw new JsonSyntaxException(cause);
          }
          throw e;
        }
      });
      assertEquals("malformed", thrown.getCause().getMessage());
      mockedStatic.verify(() -> JsonParser.parseReader(jsonReader));
    }
  }
}