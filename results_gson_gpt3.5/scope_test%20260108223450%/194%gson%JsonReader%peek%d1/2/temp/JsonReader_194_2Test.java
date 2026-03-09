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

public class JsonReader_194_2Test {

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

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private int invokeDoPeek() throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void peek_whenPeekedIsNone_callsDoPeekAndReturnsCorrectToken() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setPeeked(0);

    // Spy on jsonReader to mock doPeek method
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek to return each possible peeked value and verify returned JsonToken
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
      doReturn(peekedValues[i]).when(spyReader).doPeek();
      // Reset peeked to 0 before each call
      setPeeked(0);
      JsonToken result = spyReader.peek();
      assertEquals(expectedTokens[i], result,
          "peek() should return correct JsonToken for peeked value " + peekedValues[i]);
      // Also verify peeked field updated correctly
      assertEquals(peekedValues[i], getPeeked());
    }
  }

  @Test
    @Timeout(8000)
  public void peek_whenPeekedIsNotNone_returnsCorrespondingTokenWithoutCallingDoPeek() throws Exception {
    // Test all peeked values except PEEKED_NONE (0)
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

    JsonReader spyReader = spy(jsonReader);

    for (int i = 0; i < peekedValues.length; i++) {
      setPeeked(peekedValues[i]);
      JsonToken result = spyReader.peek();
      assertEquals(expectedTokens[i], result,
          "peek() should return correct JsonToken for peeked value " + peekedValues[i]);
      // Verify doPeek is never called
      verify(spyReader, never()).doPeek();
    }
  }

  @Test
    @Timeout(8000)
  public void peek_whenPeekedIsInvalid_throwsAssertionError() throws Exception {
    setPeeked(999); // invalid peeked value

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      jsonReader.peek();
    });
    assertNotNull(thrown);
  }
}