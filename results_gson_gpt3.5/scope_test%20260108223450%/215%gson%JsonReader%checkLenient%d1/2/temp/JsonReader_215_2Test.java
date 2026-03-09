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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_215_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testCheckLenient_LenientTrue_NoException() throws Throwable {
    // set lenient to true via public setter
    jsonReader.setLenient(true);

    // invoke private method checkLenient via reflection
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // should not throw
    assertDoesNotThrow(() -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        // unwrap underlying exception
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testCheckLenient_LenientFalse_ThrowsIOException() throws Throwable {
    // ensure lenient is false (default)
    jsonReader.setLenient(false);

    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        // unwrap underlying exception
        throw e.getCause();
      }
    });

    String expectedMessage = "Use JsonReader.setLenient(true) to accept malformed JSON";
    String actualMessage = thrown.getMessage();
    assertTrue(actualMessage.startsWith(expectedMessage),
      () -> "Expected message to start with: <" + expectedMessage + "> but was: <" + actualMessage + ">");
  }
}