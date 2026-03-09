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
import org.mockito.Mockito;

class JsonReader_194_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int callDoPeek() throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(jsonReader);
  }

  @Test
    @Timeout(8000)
  void testPeek_withPeekedNone_callsDoPeekAndReturnsToken() throws Exception {
    // Spy on jsonReader to mock doPeek()
    JsonReader spyReader = Mockito.spy(jsonReader);

    // For each possible peeked value, check correct JsonToken is returned
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

    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);

    for (int i = 0; i < peekedValues.length; i++) {
      int peekedValue = peekedValues[i];
      JsonToken expected = expectedTokens[i];

      // Reset peeked to 0 before each call on the spy's underlying instance
      peekedField.setInt(spyReader, 0);

      // Mock doPeek to return peekedValue and also set peeked field to peekedValue
      doAnswer(invocation -> {
        peekedField.setInt(spyReader, peekedValue);
        return peekedValue;
      }).when(spyReader).doPeek();

      // Invoke peek and verify returned token
      JsonToken result = spyReader.peek();
      assertEquals(expected, result, "peek() with doPeek returning " + peekedValue);

      // Verify peeked field updated correctly
      assertEquals(peekedValue, peekedField.getInt(spyReader));
    }
  }

  @Test
    @Timeout(8000)
  void testPeek_withPeekedSet_returnsCorrespondingToken() throws Exception {
    // Test all peeked values except 0 (PEEKED_NONE)
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
      int peekedValue = peekedValues[i];
      JsonToken expected = expectedTokens[i];

      setPeeked(peekedValue);

      JsonToken result = jsonReader.peek();
      assertEquals(expected, result, "peek() with peeked set to " + peekedValue);
    }
  }

  @Test
    @Timeout(8000)
  void testPeek_withInvalidPeeked_throwsAssertionError() throws Exception {
    setPeeked(-1);
    assertThrows(AssertionError.class, () -> jsonReader.peek());

    setPeeked(999);
    assertThrows(AssertionError.class, () -> jsonReader.peek());
  }
}