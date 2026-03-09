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
import org.mockito.Mockito;

class JsonReader_211_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private Object invokeDoPeek(JsonReader reader) throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    return doPeek.invoke(reader);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNone_callsDoPeekAndProcessesTokens() throws Exception {
    // Arrange: set peeked to PEEKED_NONE and mock doPeek to return PEEKED_BEGIN_ARRAY once, then PEEKED_END_ARRAY
    setField(jsonReader, "peeked", 0); // PEEKED_NONE

    JsonReader spyReader = Mockito.spy(jsonReader);

    doReturn(3) // PEEKED_BEGIN_ARRAY
      .doReturn(4) // PEEKED_END_ARRAY
      .doReturn(17) // PEEKED_EOF to end loop
      .when(spyReader).doPeek();

    // Initialize stackSize to 0, pathIndices and pathNames arrays
    setField(spyReader, "stackSize", 0);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);

    // Act
    spyReader.skipValue();

    // Assert
    int stackSize = (int) getField(spyReader, "stackSize");
    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(0, stackSize);
    assertEquals(1, pathIndices[stackSize > 0 ? stackSize - 1 : 0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_beginObjectAndEndObjectUpdatesStackAndPathNames() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    // Setup peeked to PEEKED_BEGIN_OBJECT first, then PEEKED_END_OBJECT, then PEEKED_EOF
    doReturn(1)  // PEEKED_BEGIN_OBJECT
      .doReturn(2)  // PEEKED_END_OBJECT
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    // Initialize stackSize to 1 and pathNames with non-null value at index 0
    setField(spyReader, "stackSize", 1);
    String[] pathNames = new String[32];
    pathNames[0] = "name";
    setField(spyReader, "pathNames", pathNames);
    setField(spyReader, "pathIndices", new int[32]);

    spyReader.skipValue();

    int stackSize = (int) getField(spyReader, "stackSize");
    String[] updatedPathNames = (String[]) getField(spyReader, "pathNames");

    assertEquals(0, stackSize);
    assertNull(updatedPathNames[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_unquotedName_skipsValueAndSetsPathName() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    // Setup peeked to PEEKED_UNQUOTED_NAME then PEEKED_EOF
    doReturn(14)  // PEEKED_UNQUOTED_NAME
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    // Initialize stackSize to 1 and pathNames with null at index 0
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathNames", new String[32]);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    String[] pathNames = (String[]) getField(spyReader, "pathNames");
    assertEquals("<skipped>", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_singleQuotedName_skipsValueAndSetsPathName() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    // Setup peeked to PEEKED_SINGLE_QUOTED_NAME then PEEKED_EOF
    doReturn(12)  // PEEKED_SINGLE_QUOTED_NAME
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    // Initialize stackSize to 1 and pathNames with null at index 0
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathNames", new String[32]);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    String[] pathNames = (String[]) getField(spyReader, "pathNames");
    assertEquals("<skipped>", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_doubleQuotedName_skipsValueAndSetsPathName() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    // Setup peeked to PEEKED_DOUBLE_QUOTED_NAME then PEEKED_EOF
    doReturn(13)  // PEEKED_DOUBLE_QUOTED_NAME
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    // Initialize stackSize to 1 and pathNames with null at index 0
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathNames", new String[32]);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    String[] pathNames = (String[]) getField(spyReader, "pathNames");
    assertEquals("<skipped>", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNumber_advancesPos() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    // Setup peeked to PEEKED_NUMBER then PEEKED_EOF
    doReturn(16)  // PEEKED_NUMBER
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    setField(spyReader, "pos", 5);
    setField(spyReader, "peekedNumberLength", 3);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    spyReader.skipValue();

    int pos = (int) getField(spyReader, "pos");
    assertEquals(8, pos);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedSingleQuoted_skipsQuotedValue() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    doReturn(8)  // PEEKED_SINGLE_QUOTED
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('\'');
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedDoubleQuoted_skipsQuotedValue() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    doReturn(9)  // PEEKED_DOUBLE_QUOTED
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('"');
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedUnquoted_skipsUnquotedValue() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    doReturn(10)  // PEEKED_UNQUOTED
      .doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    verify(spyReader).skipUnquotedValue();
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedEof_returnsImmediately() throws Exception {
    JsonReader spyReader = Mockito.spy(jsonReader);

    doReturn(17) // PEEKED_EOF
      .when(spyReader).doPeek();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    spyReader.skipValue();

    // No exception, method returns immediately
  }
}