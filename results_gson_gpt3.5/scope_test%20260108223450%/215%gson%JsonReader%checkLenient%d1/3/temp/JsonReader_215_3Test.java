package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_215_3Test {

  private JsonReader jsonReader;
  private Method checkLenientMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    Reader dummyReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF
      }
      @Override
      public void close() {
      }
    };
    jsonReader = new JsonReader(dummyReader);
    checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testCheckLenient_whenLenientTrue_doesNotThrow() throws Throwable {
    jsonReader.setLenient(true);
    // Should not throw IOException
    assertDoesNotThrow(() -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testCheckLenient_whenLenientFalse_throwsIOException() {
    jsonReader.setLenient(false);
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    // Optional: verify exception message
    assert(thrown.getMessage().contains("Use JsonReader.setLenient(true) to accept malformed JSON"));
  }
}