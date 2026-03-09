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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_225_5Test {
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create a JsonReader with dummy Reader (null) since we will manipulate buffer and pos directly
    jsonReader = new JsonReader(null);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private Object getField(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private Object invokePrivateMethod(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod(methodName, paramTypes);
    method.setAccessible(true);
    return method.invoke(jsonReader, args);
  }

  private void invokeNextNonWhitespace(boolean throwOnEof) throws Exception {
    invokePrivateMethod("nextNonWhitespace", new Class<?>[] {boolean.class}, throwOnEof);
  }

  private boolean invokeFillBuffer(int minimum) throws Exception {
    return (boolean) invokePrivateMethod("fillBuffer", new Class<?>[] {int.class}, minimum);
  }

  private void invokeConsumeNonExecutePrefix() throws Exception {
    invokePrivateMethod("consumeNonExecutePrefix", new Class<?>[0]);
  }

  @Test
    @Timeout(8000)
  public void testConsumeNonExecutePrefix_posDecrementedAfterNextNonWhitespace() throws Exception {
    // Setup: pos = 1, limit = 10, buffer has irrelevant data
    setField("pos", 1);
    setField("limit", 10);
    char[] buffer = new char[1024];
    buffer[0] = ' ';
    buffer[1] = 'a';
    setField("buffer", buffer);

    // Instead of spying and mocking private methods, just invoke and set pos manually
    // Simulate nextNonWhitespace advancing pos from 1 to 2
    setField("pos", 2);

    // pos should be decremented by 1 after nextNonWhitespace inside consumeNonExecutePrefix
    setField("pos", 2);
    invokeConsumeNonExecutePrefix();
    int posAfter = (int) getField("pos");
    assertEquals(1, posAfter);
  }

  @Test
    @Timeout(8000)
  public void testConsumeNonExecutePrefix_returnsIfBufferNotFilled() throws Exception {
    // pos + 5 > limit and fillBuffer returns false -> method returns early
    setField("pos", 6);
    setField("limit", 10);
    char[] buffer = new char[1024];
    setField("buffer", buffer);

    // Create a subclass to override fillBuffer
    JsonReader jsonReaderSubclass = new JsonReader(null) {
      @Override
      boolean fillBuffer(int minimum) throws IOException {
        return false;
      }
    };

    // Set the fields on the new jsonReaderSubclass instance
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.set(jsonReaderSubclass, 6);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.set(jsonReaderSubclass, 10);

    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReaderSubclass, buffer);

    // Invoke consumeNonExecutePrefix via reflection on the new instance
    Method consumeMethod = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    consumeMethod.setAccessible(true);
    consumeMethod.invoke(jsonReaderSubclass);

    int posAfter = (int) posField.get(jsonReaderSubclass);
    assertEquals(6, posAfter);
  }

  @Test
    @Timeout(8000)
  public void testConsumeNonExecutePrefix_returnsIfBufferDoesNotMatchSecurityToken() throws Exception {
    // Setup buffer with chars not matching the security token
    char[] buffer = new char[1024];
    buffer[0] = ')';
    buffer[1] = ']';
    buffer[2] = '}';
    buffer[3] = '\'';
    buffer[4] = 'x'; // last char different from '\n'
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 10);

    invokeConsumeNonExecutePrefix();

    // pos should be decremented by 1 after nextNonWhitespace (pos=0->pos=-1), then early return
    int posAfter = (int) getField("pos");
    assertEquals(-1, posAfter);
  }

  @Test
    @Timeout(8000)
  public void testConsumeNonExecutePrefix_consumesSecurityToken() throws Exception {
    // Setup buffer with exact security token ") ] } ' \n"
    char[] buffer = new char[1024];
    buffer[0] = ')';
    buffer[1] = ']';
    buffer[2] = '}';
    buffer[3] = '\'';
    buffer[4] = '\n';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 10);

    invokeConsumeNonExecutePrefix();

    // pos should be decremented by 1 after nextNonWhitespace (pos=0->pos=-1)
    // then pos +=5 for consuming security token => pos = 4
    int posAfter = (int) getField("pos");
    assertEquals(4, posAfter);
  }
}