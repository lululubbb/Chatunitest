package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

class JsonWriterOpenTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testOpen_withEmptyArray() throws Throwable {
    // empty = JsonScope.EMPTY_ARRAY = 1 (from Gson source)
    // openBracket = '['
    int emptyArray = 1;
    char openBracket = '[';

    // invoke private method open(int, char)
    JsonWriter result = invokeOpen(jsonWriter, emptyArray, openBracket);

    assertSame(jsonWriter, result);
    verify(mockWriter).write(openBracket);
  }

  @Test
    @Timeout(8000)
  public void testOpen_withEmptyObject() throws Throwable {
    // empty = JsonScope.EMPTY_OBJECT = 3 (from Gson source)
    // openBracket = '{'
    int emptyObject = 3;
    char openBracket = '{';

    JsonWriter result = invokeOpen(jsonWriter, emptyObject, openBracket);

    assertSame(jsonWriter, result);
    verify(mockWriter).write(openBracket);
  }

  @Test
    @Timeout(8000)
  public void testOpen_beforeValueThrowsIOException() throws Throwable {
    // Create a subclass of JsonWriter that overrides beforeValue to throw IOException
    JsonWriter jsonWriterWithException = new JsonWriter(mockWriter) {
      @Override
      @SuppressWarnings("java:S112") // allow throwing IOException for test
      private void beforeValue() throws IOException {
        throw new IOException("beforeValue error");
      }
    };

    // Since beforeValue is private, the override above will not work directly.
    // Instead, use reflection to create a proxy that throws IOException from beforeValue.
    // Because beforeValue is private, overriding it is not possible.
    // So we create a spy and use a spy subclass that overrides beforeValue via reflection.

    // Use a spy on the subclass that overrides beforeValue via reflection.
    JsonWriter spyWriter = spy(jsonWriterWithException);

    // Use reflection to make beforeValue accessible and stub it to throw IOException
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Use Mockito doThrow on spyWriter for beforeValue method call via reflection
    doAnswer(invocation -> {
      throw new IOException("beforeValue error");
    }).when(spyWriter).getClass().getDeclaredMethod("beforeValue").invoke(spyWriter);

    // The above doAnswer does not work because private methods cannot be stubbed by Mockito.
    // So the best approach is to create a subclass that overrides beforeValue as public and use that.

    // So we create a subclass with a public beforeValue method throwing IOException
    class JsonWriterWithBeforeValueException extends JsonWriter {
      public JsonWriterWithBeforeValueException(Writer out) {
        super(out);
      }

      @Override
      public void flush() throws IOException {
        super.flush();
      }

      // We need to expose beforeValue as public for overriding
      public void beforeValue() throws IOException {
        throw new IOException("beforeValue error");
      }
    }

    JsonWriterWithBeforeValueException writerWithException = new JsonWriterWithBeforeValueException(mockWriter);

    // Now invoke open via reflection and catch IOException from overridden beforeValue
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        openMethod.invoke(writerWithException, 1, '[');
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw cause;
        } else {
          throw e;
        }
      }
    });

    assertEquals("beforeValue error", thrown.getMessage());
  }

  private JsonWriter invokeOpen(JsonWriter writer, int empty, char openBracket) throws Throwable {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);
    try {
      return (JsonWriter) openMethod.invoke(writer, empty, openBracket);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}