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

class JsonReader_isLiteralTest {

  private JsonReader jsonReader;
  private Method isLiteralMethod;
  private Method checkLenientMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    // Create JsonReader instance with a dummy Reader (using empty Reader to avoid NPE)
    jsonReader = new JsonReader(new Reader() {
      @Override public int read(char[] cbuf, int off, int len) { return -1; }
      @Override public void close() {}
    });
    // Access private isLiteral method via reflection
    isLiteralMethod = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteralMethod.setAccessible(true);

    // Access private checkLenient method via reflection
    checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);
  }

  private boolean invokeIsLiteral(char c) throws Throwable {
    try {
      return (boolean) isLiteralMethod.invoke(jsonReader, c);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseForWhitespaceChars() throws Throwable {
    assertFalse(invokeIsLiteral(' '));
    assertFalse(invokeIsLiteral('\t'));
    assertFalse(invokeIsLiteral('\f'));
    assertFalse(invokeIsLiteral('\r'));
    assertFalse(invokeIsLiteral('\n'));
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseForJsonStructuralChars() throws Throwable {
    char[] chars = {'{', '}', '[', ']', ':', ','};
    for (char c : chars) {
      assertFalse(invokeIsLiteral(c), "Expected false for char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_callsCheckLenientAndReturnsFalseForLenientChars() throws Throwable {
    // The chars that call checkLenient and then fall through to return false
    char[] lenientChars = {'/', '\\', ';', '#', '='};

    for (char c : lenientChars) {
      // Create a new JsonReader instance for each iteration to avoid state issues
      JsonReader spyReader = spy(new JsonReader(new Reader() {
        @Override public int read(char[] cbuf, int off, int len) { return -1; }
        @Override public void close() {}
      }));

      Method method = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
      method.setAccessible(true);

      // Set lenient to false and expect IOException
      spyReader.setLenient(false);

      IOException thrown = assertThrows(IOException.class, () -> {
        try {
          method.invoke(spyReader, c);
        } catch (InvocationTargetException e) {
          throw e.getCause();
        }
      }, "Expected IOException when lenient is false for char: " + c);

      // Now set lenient to true and verify it returns false without exception
      spyReader.setLenient(true);
      boolean result = (boolean) method.invoke(spyReader, c);
      assertFalse(result, "Expected false for lenient char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsTrueForOtherChars() throws Throwable {
    // Characters that should return true (anything not in the cases)
    char[] trueChars = {'a', 'Z', '0', '9', '_', '+', '-', '*', '!', '@', '%', '^', '&', '|', '~', '`', '<', '>'};

    for (char c : trueChars) {
      assertTrue(invokeIsLiteral(c), "Expected true for char: " + c);
    }
  }
}