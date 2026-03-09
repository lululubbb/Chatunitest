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

class JsonReader_215_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void checkLenient_lenientTrue_doesNotThrow() throws Throwable {
    // Set lenient to true using public setter
    jsonReader.setLenient(true);

    // Use reflection to invoke private method checkLenient
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // Should not throw any exception
    assertDoesNotThrow(() -> checkLenientMethod.invoke(jsonReader));
  }

  @Test
    @Timeout(8000)
  void checkLenient_lenientFalse_throwsIOException() throws Throwable {
    // Ensure lenient is false (default)
    jsonReader.setLenient(false);

    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> checkLenientMethod.invoke(jsonReader));

    // The cause should be an IOException with expected message start
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().startsWith("Use JsonReader.setLenient(true) to accept malformed JSON"));
  }
}