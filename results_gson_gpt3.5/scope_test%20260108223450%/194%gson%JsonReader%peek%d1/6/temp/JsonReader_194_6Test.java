package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_194_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int invokeDoPeek() throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(jsonReader);
  }

  private void setPeekedOn(Object target, int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(target, value);
  }

  private int getPeeked(Object target) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(target);
  }

  @Test
    @Timeout(8000)
  public void testPeek_whenPeekedIsNone_callsDoPeekAndReturnsCorrectToken() throws Exception {
    // Spy on jsonReader to mock doPeek method
    JsonReader spyReader = spy(jsonReader);

    // For each peeked value, mock doPeek to return it and verify peek() returns correct JsonToken
    int[] peekedValues = {
        1, 2, 3, 4, // BEGIN/END OBJECT/ARRAY
        12, 13, 14, // NAME tokens
        5, 6,       // BOOLEAN tokens
        7,          // NULL token
        8, 9, 10, 11, // STRING tokens
        15, 16,     // NUMBER tokens
        17          // END_DOCUMENT token
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
      // Reset peeked to PEEKED_NONE before each call
      setPeekedOn(spyReader, 0);
      doReturn(peekedValues[i]).when(spyReader).doPeek();

      JsonToken token = spyReader.peek();
      assertEquals(expectedTokens[i], token, "peek() should return correct JsonToken for peeked value " + peekedValues[i]);

      // Verify that after peek, peeked field is set to the value returned by doPeek
      int peekedValue = getPeeked(spyReader);
      assertEquals(peekedValues[i], peekedValue);

      clearInvocations(spyReader);
    }
  }

  @Test
    @Timeout(8000)
  public void testPeek_whenPeekedIsNotNone_returnsCorrespondingToken() throws Exception {
    int[] peekedValues = {
        1, 2, 3, 4, // BEGIN/END OBJECT/ARRAY
        12, 13, 14, // NAME tokens
        5, 6,       // BOOLEAN tokens
        7,          // NULL token
        8, 9, 10, 11, // STRING tokens
        15, 16,     // NUMBER tokens
        17          // END_DOCUMENT token
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
      setPeeked(peekedValues[i]);
      JsonToken token = jsonReader.peek();
      assertEquals(expectedTokens[i], token, "peek() should return correct JsonToken for peeked value " + peekedValues[i]);
    }
  }

  @Test
    @Timeout(8000)
  public void testPeek_whenPeekedIsInvalid_throwsAssertionError() throws Exception {
    setPeeked(999);
    AssertionError error = assertThrows(AssertionError.class, () -> jsonReader.peek());
    assertNotNull(error);
  }
}