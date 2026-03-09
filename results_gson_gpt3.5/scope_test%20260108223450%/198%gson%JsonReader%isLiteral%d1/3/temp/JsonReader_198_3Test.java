package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_198_3Test {

  private JsonReader jsonReader;
  private Method isLiteralMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(mock(java.io.Reader.class));
    isLiteralMethod = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteralMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testIsLiteral_returnsFalseForControlCharsWithoutLenient() throws Throwable {
    char[] chars = new char[] {
        '{', '}', '[', ']', ':', ',', ' ', '\t', '\f', '\r', '\n'
    };
    for (char c : chars) {
      boolean result = invokeIsLiteral(c);
      assertFalse(result, "Expected false for char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  public void testIsLiteral_returnsFalseForControlCharsWithLenient() throws Throwable {
    jsonReader.setLenient(true);
    char[] chars = new char[] {
        '/', '\\', ';', '#', '='
    };
    for (char c : chars) {
      boolean result = invokeIsLiteral(c);
      assertFalse(result, "Expected false for char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  public void testIsLiteral_throwsIOExceptionWhenLenientIsFalseForLenientChars() {
    char[] chars = new char[] {
        '/', '\\', ';', '#', '='
    };
    for (char c : chars) {
      IOException thrown = assertThrows(IOException.class, () -> invokeIsLiteral(c));
      assertTrue(thrown.getMessage().startsWith("Use JsonReader.setLenient(true) to accept malformed JSON"),
          "Unexpected exception message: " + thrown.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testIsLiteral_returnsTrueForOtherChars() throws Throwable {
    char[] chars = new char[] {
        'a', 'Z', '0', '9', '_', '+', '-', '*', '%', '@'
    };
    for (char c : chars) {
      boolean result = invokeIsLiteral(c);
      assertTrue(result, "Expected true for char: " + c);
    }
  }

  // Helper method to invoke private isLiteral method and unwrap exceptions
  private boolean invokeIsLiteral(char c) throws Throwable {
    try {
      return (boolean) isLiteralMethod.invoke(jsonReader, c);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}