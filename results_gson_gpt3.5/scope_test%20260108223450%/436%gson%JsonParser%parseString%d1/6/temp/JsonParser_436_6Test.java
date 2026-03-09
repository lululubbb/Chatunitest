package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.MalformedJsonException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_436_6Test {

  @Test
    @Timeout(8000)
  public void testParseString_validJson_returnsJsonElement() {
    String json = "{\"key\":\"value\"}";
    JsonElement element = JsonParser.parseString(json);
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testParseString_emptyJson_throwsJsonSyntaxException() {
    String json = " ";
    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      JsonParser.parseString(json);
    });
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseString_invalidJson_throwsJsonSyntaxException() {
    String json = "{key:value"; // malformed json
    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      JsonParser.parseString(json);
    });
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseReader_withMockedReader_invokesPrivateParseReader() throws Exception {
    String json = "{\"mock\":\"test\"}";
    StringReader stringReader = spy(new StringReader(json));

    // Use reflection to get the public static parseReader(Reader) method
    Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", Reader.class);
    parseReaderMethod.setAccessible(true);

    // Invoke the public static method
    Object result = parseReaderMethod.invoke(null, stringReader);
    assertNotNull(result);
    assertTrue(result instanceof JsonElement);
  }

  @Test
    @Timeout(8000)
  public void testParseReader_JsonReader_mockedBehavior() throws IOException, NoSuchMethodException, IllegalAccessException {
    JsonReader mockReader = mock(JsonReader.class);

    when(mockReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.END_OBJECT, JsonToken.END_DOCUMENT);
    when(mockReader.nextName()).thenReturn("key");
    when(mockReader.nextString()).thenReturn("value");
    doNothing().when(mockReader).beginObject();
    doNothing().when(mockReader).endObject();

    // Use reflection to get the public static parseReader(JsonReader) method
    Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
    parseReaderMethod.setAccessible(true);

    try {
      Object element = parseReaderMethod.invoke(null, mockReader);
      assertNotNull(element);
      assertTrue(element instanceof JsonElement);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      fail("Invocation threw exception: " + cause);
    }
  }
}