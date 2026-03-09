package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_224_1Test {

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
  public void syntaxError_throwsMalformedJsonExceptionWithMessageAndLocation() throws Throwable {
    String message = "Test error message";

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      try {
        syntaxErrorMethod.invoke(jsonReader, message);
      } catch (InvocationTargetException e) {
        throw e;
      }
    });

    Throwable cause = thrown.getCause();

    // Check that the cause is a MalformedJsonException
    assertEquals("com.google.gson.stream.MalformedJsonException", cause.getClass().getName());

    // The message should start with the provided message and include the location string
    String causeMessage = cause.getMessage();
    // The locationString() is package-private, but it is called in the message, so check it ends with locationString()
    // locationString() returns a String that starts with " at line " or similar; we check that message starts with our message
    // and the full message length is greater than the message length
    // This ensures locationString() is appended
    assertEquals(true, causeMessage.startsWith(message));
    assertEquals(true, causeMessage.length() > message.length());
  }
}