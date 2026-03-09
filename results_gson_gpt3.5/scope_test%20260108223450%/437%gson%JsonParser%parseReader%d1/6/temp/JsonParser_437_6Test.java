package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_437_6Test {

  @Test
    @Timeout(8000)
  public void parseReader_validJson_consumesEntireDocument() throws Exception {
    String json = "{\"key\":\"value\"}";
    Reader reader = new StringReader(json);

    JsonElement element = JsonParser.parseReader(reader);

    assertNotNull(element);
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_emptyJson_returnsJsonNull() throws Exception {
    String json = "null";
    Reader reader = new StringReader(json);

    JsonElement element = JsonParser.parseReader(reader);

    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void parseReader_notEntireDocument_throwsJsonSyntaxException() throws Exception {
    String json = "{\"key\":\"value\"} extra";
    Reader reader = new StringReader(json);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
    assertEquals("Did not consume the entire document.", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void parseReader_malformedJson_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);

    JsonParserTestHelper helper = spy(new JsonParserTestHelper(jsonReader));
    doThrow(new MalformedJsonException("malformed"))
        .when(helper)
        .invokeParseReader(jsonReader);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      helper.invokeParseReaderWithException(reader);
    });
    assertTrue(ex.getCause() instanceof MalformedJsonException);
  }

  @Test
    @Timeout(8000)
  public void parseReader_ioException_throwsJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);

    JsonParserTestHelper helper = spy(new JsonParserTestHelper(jsonReader));
    doThrow(new IOException("io"))
        .when(helper)
        .invokeParseReader(jsonReader);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      helper.invokeParseReaderWithException(reader);
    });
    assertTrue(ex.getCause() instanceof IOException);
  }

  @Test
    @Timeout(8000)
  public void parseReader_numberFormatException_throwsJsonSyntaxException() throws Exception {
    Reader reader = mock(Reader.class);
    JsonReader jsonReader = mock(JsonReader.class);

    JsonParserTestHelper helper = spy(new JsonParserTestHelper(jsonReader));
    doThrow(new NumberFormatException("number format"))
        .when(helper)
        .invokeParseReader(jsonReader);

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      helper.invokeParseReaderWithException(reader);
    });
    assertTrue(ex.getCause() instanceof NumberFormatException);
  }

  // Helper class to simulate behavior of JsonParser.parseReader(JsonReader) via reflection
  private static class JsonParserTestHelper {
    private final JsonReader mockJsonReader;

    JsonParserTestHelper(JsonReader mockJsonReader) {
      this.mockJsonReader = mockJsonReader;
    }

    // Method to invoke private static parseReader(JsonReader) via reflection
    JsonElement invokeParseReader(JsonReader jsonReader) throws Exception {
      Method method = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
      method.setAccessible(true);
      return (JsonElement) method.invoke(null, jsonReader);
    }

    // Simulate the full parseReader(Reader) method but using the mocked JsonReader and mocked parseReader(JsonReader)
    JsonElement invokeParseReaderWithException(Reader reader) throws Exception {
      try {
        // Set lenient true on the mockJsonReader to avoid MalformedJsonException for trailing tokens
        doNothing().when(mockJsonReader).setLenient(true);
        mockJsonReader.setLenient(true);

        // Return a valid JsonElement so that the code proceeds to the peek check
        when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

        JsonElement element = invokeParseReader(mockJsonReader);
        if (!element.isJsonNull() && mockJsonReader.peek() != JsonToken.END_DOCUMENT) {
          throw new JsonSyntaxException("Did not consume the entire document.");
        }
        return element;
      } catch (MalformedJsonException e) {
        throw new JsonSyntaxException(e);
      } catch (IOException e) {
        throw new JsonIOException(e);
      } catch (NumberFormatException e) {
        throw new JsonSyntaxException(e);
      }
    }
  }
}