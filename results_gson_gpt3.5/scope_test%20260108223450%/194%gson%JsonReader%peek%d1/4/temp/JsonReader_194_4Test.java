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

public class JsonReader_194_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testPeek_withPeekedNone_callsDoPeekAndReturnsCorrectToken() throws Exception {
    setPeeked(0); // PEEKED_NONE

    // Mock doPeek to return each possible peeked value and verify returned JsonToken
    int[] peekedValues = {
        1, 2, 3, 4,
        12, 13, 14,
        5, 6,
        7,
        8, 9, 10, 11,
        15, 16,
        17
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

    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    for (int i = 0; i < peekedValues.length; i++) {
      int peekedValue = peekedValues[i];
      JsonToken expected = expectedTokens[i];

      // Spy on jsonReader to override doPeek()
      JsonReader spyReader = spy(jsonReader);
      doReturn(peekedValue).when(spyReader).doPeek();

      setPeeked(spyReader, 0); // Reset peeked to PEEKED_NONE to force doPeek call
      JsonToken actual = spyReader.peek();
      assertEquals(expected, actual, "peeked=" + peekedValue);
    }
  }

  @Test
    @Timeout(8000)
  public void testPeek_withPeekedSetReturnsCorrespondingToken() throws Exception {
    // When peeked is set to a value other than PEEKED_NONE, doPeek should not be called
    int[] peekedValues = {
        1, 2, 3, 4,
        12, 13, 14,
        5, 6,
        7,
        8, 9, 10, 11,
        15, 16,
        17
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
      int peekedValue = peekedValues[i];
      JsonToken expected = expectedTokens[i];

      JsonReader spyReader = spy(jsonReader);
      setPeeked(spyReader, peekedValue);

      // doPeek should not be called
      doReturn(-1).when(spyReader).doPeek();

      JsonToken actual = spyReader.peek();
      assertEquals(expected, actual, "peeked=" + peekedValue);
      verify(spyReader, never()).doPeek();
    }
  }

  @Test
    @Timeout(8000)
  public void testPeek_withInvalidPeeked_throwsAssertionError() throws Exception {
    JsonReader spyReader = spy(jsonReader);
    setPeeked(spyReader, 999); // invalid peeked value
    doReturn(999).when(spyReader).doPeek();

    AssertionError thrown = assertThrows(AssertionError.class, spyReader::peek);
    assertNotNull(thrown);
  }

  private void setPeeked(int value) {
    try {
      Field peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      peekedField.setInt(jsonReader, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPeeked(JsonReader instance, int value) {
    try {
      Field peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      peekedField.setInt(instance, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}