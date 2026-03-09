package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_224_6Test {

  private JsonReader jsonReader;
  private Method syntaxErrorMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    Reader reader = new StringReader("");
    jsonReader = new JsonReader(reader);
    syntaxErrorMethod = JsonReader.class.getDeclaredMethod("syntaxError", String.class);
    syntaxErrorMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testSyntaxError_throwsMalformedJsonExceptionWithMessageAndLocation() {
    String message = "Test error message";

    try {
      syntaxErrorMethod.invoke(jsonReader, message);
      fail("Expected InvocationTargetException wrapping MalformedJsonException");
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      // The private syntaxError method throws MalformedJsonException which extends IOException
      if (cause instanceof MalformedJsonException) {
        MalformedJsonException ex = (MalformedJsonException) cause;
        String expectedStart = message;
        String actualMessage = ex.getMessage();
        // The message should start with the given message and contain location string
        assertEquals(true, actualMessage.startsWith(expectedStart));
        // locationString() returns a non-null string appended to message, so message length > message param length
        assertEquals(true, actualMessage.length() > message.length());
      } else {
        fail("Expected MalformedJsonException but got: " + cause);
      }
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }
}