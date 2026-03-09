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

public class JsonReader_199_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int PEEKED_NONE = 0;
  private static final int PEEKED_UNQUOTED_NAME = 14;
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
  private static final int PEEKED_BEGIN_OBJECT = 1;

  @BeforeEach
  public void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Set stackSize to 1 for pathNames indexing in nextName
    setField(jsonReader, "stackSize", 1);
    // Initialize pathNames array with length at least 1
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
  }

  @Test
    @Timeout(8000)
  public void testNextName_whenPeekedIsNone_callsDoPeekAndReturnsUnquotedName() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", PEEKED_NONE);

    JsonReader spyReader = spy(jsonReader);

    // Use reflection to mock private doPeek() method
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    doReturn(PEEKED_UNQUOTED_NAME).when(spyReader).doPeek();

    // Use reflection to mock private nextUnquotedValue() method
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    doReturn("unquotedName").when(spyReader).nextUnquotedValue();

    // Act
    String result = spyReader.nextName();

    // Assert
    assertEquals("unquotedName", result);
    // Cast to int explicitly to avoid assertEquals ambiguity
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    String[] pathNames = getField(spyReader, "pathNames");
    int stackSize = getField(spyReader, "stackSize");
    assertEquals("unquotedName", pathNames[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextName_whenPeekedIsUnquotedName_returnsNextUnquotedValue() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", PEEKED_UNQUOTED_NAME);

    JsonReader spyReader = spy(jsonReader);

    // Use reflection to mock private nextUnquotedValue() method
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    doReturn("unquotedName").when(spyReader).nextUnquotedValue();

    // Act
    String result = spyReader.nextName();

    // Assert
    assertEquals("unquotedName", result);
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    String[] pathNames = getField(spyReader, "pathNames");
    int stackSize = getField(spyReader, "stackSize");
    assertEquals("unquotedName", pathNames[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextName_whenPeekedIsSingleQuotedName_returnsNextQuotedValueSingleQuote() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", PEEKED_SINGLE_QUOTED_NAME);

    JsonReader spyReader = spy(jsonReader);

    // Use reflection to mock private nextQuotedValue(char) method
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    doReturn("singleQuotedName").when(spyReader).nextQuotedValue('\'');

    // Act
    String result = spyReader.nextName();

    // Assert
    assertEquals("singleQuotedName", result);
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    String[] pathNames = getField(spyReader, "pathNames");
    int stackSize = getField(spyReader, "stackSize");
    assertEquals("singleQuotedName", pathNames[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextName_whenPeekedIsDoubleQuotedName_returnsNextQuotedValueDoubleQuote() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", PEEKED_DOUBLE_QUOTED_NAME);

    JsonReader spyReader = spy(jsonReader);

    // Use reflection to mock private nextQuotedValue(char) method
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    doReturn("doubleQuotedName").when(spyReader).nextQuotedValue('\"');

    // Act
    String result = spyReader.nextName();

    // Assert
    assertEquals("doubleQuotedName", result);
    assertEquals((int) PEEKED_NONE, (int) getField(spyReader, "peeked"));
    String[] pathNames = getField(spyReader, "pathNames");
    int stackSize = getField(spyReader, "stackSize");
    assertEquals("doubleQuotedName", pathNames[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextName_whenPeekedIsInvalid_throwsIllegalStateException() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", PEEKED_BEGIN_OBJECT); // invalid for nextName

    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at line 0 column 0 path $").when(spyReader).locationString();

    // Act & Assert
    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextName);
    assertTrue(ex.getMessage().contains("Expected a name but was"));
    assertTrue(ex.getMessage().contains("BEGIN_OBJECT"));
  }

  // Helper methods to access private fields with reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object obj, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(obj);
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }
}