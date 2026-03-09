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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonReader_186_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withNullReader_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      new JsonReader(null);
    });
    assertEquals("in == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withValidReader_initializesFields() throws Exception {
    Reader mockReader = mock(Reader.class);
    JsonReader jsonReader = new JsonReader(mockReader);

    // Using reflection to verify private final field 'in'
    java.lang.reflect.Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    Reader inValue = (Reader) inField.get(jsonReader);
    assertSame(mockReader, inValue);

    // Verify initial state of peeked field
    java.lang.reflect.Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    int peekedValue = peekedField.getInt(jsonReader);
    // Should be PEEKED_NONE (0)
    assertEquals(0, peekedValue);

    // Verify initial pos and limit are zero
    java.lang.reflect.Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(0, pos);

    java.lang.reflect.Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    int limit = limitField.getInt(jsonReader);
    assertEquals(0, limit);

    // Verify lenient default false
    java.lang.reflect.Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(jsonReader);
    assertFalse(lenient);
  }
}