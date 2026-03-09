package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReaderPeekTest {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    // Mock Reader (not used by peek directly)
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Set lenient to true to avoid leniency issues if any
    jsonReader.setLenient(true);
  }

  private void setPeeked(Object target, int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(target, value);
  }

  private int getPeeked(Object target) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(target);
  }

  private int invokeDoPeek(Object target) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(target);
  }

  @Test
    @Timeout(8000)
  void peek_returnsPeekedToken_whenPeekedIsNotNone() throws Exception {
    // Set peeked to each known value except 0 (PEEKED_NONE) and test the returned JsonToken
    // Mapping from peeked int to expected JsonToken
    int[] peekedValues = {
        1, 2, 3, 4, 12, 13, 14, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17
    };
    JsonToken[] expectedTokens = {
        JsonToken.BEGIN_OBJECT,
        JsonToken.END_OBJECT,
        JsonToken.BEGIN_ARRAY,
        JsonToken.END_ARRAY,
        JsonToken.NAME,
        JsonToken.NAME,
        JsonToken.NAME,
        JsonToken.BOOLEAN,
        JsonToken.BOOLEAN,
        JsonToken.NULL,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.NUMBER,
        JsonToken.NUMBER,
        JsonToken.END_DOCUMENT
    };

    for (int i = 0; i < peekedValues.length; i++) {
      setPeeked(jsonReader, peekedValues[i]);
      JsonToken token = jsonReader.peek();
      assertEquals(expectedTokens[i], token,
          "peek() with peeked=" + peekedValues[i] + " should return " + expectedTokens[i]);
    }
  }

  @Test
    @Timeout(8000)
  void peek_callsDoPeek_whenPeekedIsNone() throws Exception {
    setPeeked(jsonReader, 0); // PEEKED_NONE

    // Spy on jsonReader to mock doPeek method
    JsonReader spyReader = spy(jsonReader);

    // Use reflection to set peeked field in spyReader to 0 as well
    setPeeked(spyReader, 0);

    // Mock doPeek to return PEEKED_BEGIN_OBJECT = 1
    doReturn(1).when(spyReader).doPeek();

    JsonToken token = spyReader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, token);

    // Verify doPeek was called once
    verify(spyReader, times(1)).doPeek();

    // After peek, peeked field should be updated to 1
    int peekedValue = getPeeked(spyReader);
    assertEquals(1, peekedValue);
  }

  @Test
    @Timeout(8000)
  void peek_throwsAssertionError_onInvalidPeekedValue() throws Exception {
    // Use a subclass to override doPeek to avoid IOException on invalid peeked value
    JsonReader invalidPeekReader = new JsonReader(mock(Reader.class)) {
      @Override
      int doPeek() {
        return 999; // invalid value
      }
    };
    invalidPeekReader.setLenient(true);

    // Set peeked to 0 to force call to doPeek()
    setPeeked(invalidPeekReader, 0);

    AssertionError thrown = assertThrows(AssertionError.class, invalidPeekReader::peek);
    assertNotNull(thrown);
  }
}