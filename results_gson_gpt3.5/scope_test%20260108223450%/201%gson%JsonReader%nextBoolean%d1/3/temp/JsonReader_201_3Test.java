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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_201_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static int PEEKED_NONE;
  private static int PEEKED_TRUE;
  private static int PEEKED_FALSE;

  @BeforeEach
  public void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Access private static final fields via reflection
    PEEKED_NONE = getStaticFinalIntField(JsonReader.class, "PEEKED_NONE");
    PEEKED_TRUE = getStaticFinalIntField(JsonReader.class, "PEEKED_TRUE");
    PEEKED_FALSE = getStaticFinalIntField(JsonReader.class, "PEEKED_FALSE");
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedTrue_returnsTrue() throws Exception {
    setPeeked(PEEKED_TRUE);
    setStackSize(1);
    setPathIndices(0, 0);

    boolean result = jsonReader.nextBoolean();

    assertTrue(result);
    assertEquals((int) PEEKED_NONE, (int) getPeeked());
    assertEquals(1, getPathIndices(0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedFalse_returnsFalse() throws Exception {
    setPeeked(PEEKED_FALSE);
    setStackSize(1);
    setPathIndices(0, 5);

    boolean result = jsonReader.nextBoolean();

    assertFalse(result);
    assertEquals((int) PEEKED_NONE, (int) getPeeked());
    assertEquals(6, getPathIndices(0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedNone_callsDoPeekAndReturnsTrue() throws Exception {
    setPeeked(PEEKED_NONE);
    setStackSize(1);
    setPathIndices(0, 0);

    // Mock doPeek to return PEEKED_TRUE
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doPeekMethod.invoke(spyReader); // to initialize internals if needed
    doReturn(PEEKED_TRUE).when(spyReader).doPeek();

    setField(spyReader, "peeked", PEEKED_NONE);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[]{0});

    boolean result = spyReader.nextBoolean();

    assertTrue(result);
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedNone_callsDoPeekAndReturnsFalse() throws Exception {
    setPeeked(PEEKED_NONE);
    setStackSize(1);
    setPathIndices(0, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(PEEKED_FALSE).when(spyReader).doPeek();

    setField(spyReader, "peeked", PEEKED_NONE);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[]{0});

    boolean result = spyReader.nextBoolean();

    assertFalse(result);
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(999);
    setStackSize(1);
    setPathIndices(0, 0);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.nextBoolean());
    assertTrue(thrown.getMessage().contains("Expected a boolean but was"));
  }

  // Helper methods to access private fields via reflection

  private void setPeeked(int value) throws Exception {
    setField(jsonReader, "peeked", value);
  }

  private int getPeeked() throws Exception {
    return getField(jsonReader, "peeked");
  }

  private void setStackSize(int value) throws Exception {
    setField(jsonReader, "stackSize", value);
  }

  private void setPathIndices(int index, int value) throws Exception {
    int[] pathIndices = getField(jsonReader, "pathIndices");
    if (pathIndices == null) {
      pathIndices = new int[32];
      setField(jsonReader, "pathIndices", pathIndices);
    }
    pathIndices[index] = value;
  }

  private int getPathIndices(int index) throws Exception {
    int[] pathIndices = getField(jsonReader, "pathIndices");
    return pathIndices[index];
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private static int getStaticFinalIntField(Class<?> clazz, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(null);
  }
}