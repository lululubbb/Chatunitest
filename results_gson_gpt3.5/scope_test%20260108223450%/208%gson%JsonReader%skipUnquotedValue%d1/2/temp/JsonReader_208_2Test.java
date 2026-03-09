package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonReader_208_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_shouldReturnImmediatelyOnDelimiter() throws Throwable {
    // Setup buffer with delimiter at pos
    setField(jsonReader, "buffer", new char[] {',', 'a', 'b'});
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);

    invokeSkipUnquotedValue();

    // pos should remain 0 because delimiter ',' is at buffer[pos]
    assertEquals(0, (int) getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_shouldAdvancePosUntilDelimiter() throws Throwable {
    // Setup buffer with no delimiter at pos, but delimiter at pos + 2
    setField(jsonReader, "buffer", new char[] {'a', 'b', ' ', 'c'});
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 4);

    invokeSkipUnquotedValue();

    // pos should be advanced by 2 (to the space char)
    assertEquals(2, (int) getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_shouldCallCheckLenientForCertainChars() throws Throwable {
    // Setup buffer with '/' at pos so checkLenient is called
    setField(jsonReader, "buffer", new char[] {'/', 'a', 'b'});
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);

    // Spy on jsonReader to verify checkLenient call
    JsonReader spyReader = spy(jsonReader);

    // Replace jsonReader with spy for reflection invocation
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(spyReader);

    // pos should remain 0 because '/' is a delimiter triggering return
    assertEquals(0, (int) getField(spyReader, "pos"));

    // Verify private method checkLenient was called via reflection
    verifyPrivateMethodCalled(spyReader, "checkLenient");
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_shouldFillBufferIfNoDelimiterFound() throws Throwable {
    // Setup buffer with no delimiter and pos + i == limit
    char[] buffer = new char[1024];
    for (int i = 0; i < buffer.length; i++) {
      buffer[i] = 'a';
    }
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", buffer.length);

    // Create spyReader as spy of jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Mock private fillBuffer method via reflection
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Use doAnswer to mock private method calls
    // We need to mock fillBuffer(1) to return true once, then false
    final boolean[] firstCall = {true};
    doAnswer(invocation -> {
      int arg = invocation.getArgument(0);
      if (arg == 1) {
        if (firstCall[0]) {
          firstCall[0] = false;
          return true;
        } else {
          return false;
        }
      }
      // fallback to real method for other arguments
      return fillBufferMethod.invoke(spyReader, arg);
    }).when(spyReader).fillBuffer(1);

    // Invoke skipUnquotedValue via reflection on spyReader
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(spyReader);

    // pos should be incremented by buffer length after first loop
    assertEquals(buffer.length, (int) getField(spyReader, "pos"));

    // Verify fillBuffer(int) was called twice via reflection
    // Since fillBuffer is private, verify() won't work directly.
    // Instead, we verify by counting invocations with Mockito's verify(spyReader, atLeast(1)) but limited to fillBuffer(1)
    // So we verify at least two calls to fillBuffer(1) by using Mockito's verify(spyReader, times(2))
    // But since fillBuffer is private, this won't compile.
    // Instead, we verify via Mockito's invocation count on the spy's invocation handler:

    // Use Mockito's invocation count on spyReader's method "fillBuffer"
    // This requires reflection to access invocation count, which is complicated.
    // So as a workaround, we can verify that method was called twice by using Mockito's verify(spyReader, times(2)).fillBuffer(1)
    // But this causes compilation error because fillBuffer is private.
    // Therefore, we can check invocation count via Mockito's mocking details:

    int fillBufferCallCount = Mockito.mockingDetails(spyReader).getInvocations().stream()
        .filter(invocation -> invocation.getMethod().getName().equals("fillBuffer")
            && invocation.getArguments().length == 1
            && invocation.getArgument(0).equals(1))
        .mapToInt(i -> 1)
        .sum();

    assertEquals(2, fillBufferCallCount);
  }

  // Helper to invoke private skipUnquotedValue method
  private void invokeSkipUnquotedValue() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    try {
      method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Reflection helpers to get/set private fields
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      T value = (T) field.get(target);
      return value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to verify private void method was called on spy
  private void verifyPrivateMethodCalled(Object spy, String methodName) {
    try {
      Method method = spy.getClass().getSuperclass().getDeclaredMethod(methodName);
      method.setAccessible(true);
      // Mockito cannot verify private method calls directly, so we use a workaround:
      // We create a spy with a spy on the private method using doCallRealMethod and verify invocation count
      // But since Mockito doesn't support this directly, we can use a custom InvocationHandler or PowerMock.
      // Here, we do a simple reflection call to assert it can be invoked, but no direct verify.
      // So instead, we rely on the code coverage or manual inspection.

      // Alternatively, we can use Mockito's verify(spy, times(1)) on a public method that calls checkLenient,
      // but since checkLenient is private and called only inside skipUnquotedValue, no direct way.

      // So throw UnsupportedOperationException to indicate limitation:
      throw new UnsupportedOperationException("Cannot verify private method calls with Mockito directly.");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

}