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

class JsonReader_215_4Test {
  private JsonReader jsonReader;
  private Reader mockReader;
  private Method checkLenientMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void checkLenient_whenLenientTrue_doesNotThrow() {
    jsonReader.setLenient(true);
    assertDoesNotThrow(() -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        // unwrap
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void checkLenient_whenLenientFalse_throwsIOException() {
    jsonReader.setLenient(false);
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        checkLenientMethod.invoke(jsonReader);
      } catch (InvocationTargetException e) {
        // unwrap to get the actual IOException thrown by checkLenient()
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw cause;
        }
        throw e;
      }
    });
    assertTrue(thrown.getMessage().startsWith("Use JsonReader.setLenient(true) to accept malformed JSON"));
  }
}