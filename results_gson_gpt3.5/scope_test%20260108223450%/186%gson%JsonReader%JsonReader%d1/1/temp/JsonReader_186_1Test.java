package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;

import org.junit.jupiter.api.Test;

public class JsonReader_186_1Test {

  @Test
    @Timeout(8000)
  void constructor_withNullReader_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      new JsonReader(null);
    });
    assertEquals("in == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void constructor_withValidReader_initializesFields() throws Exception {
    Reader mockReader = mock(Reader.class);
    JsonReader jsonReader = new JsonReader(mockReader);

    // Use reflection to check private final field 'in'
    var inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    Reader inValue = (Reader) inField.get(jsonReader);
    assertSame(mockReader, inValue);

    // Check initial values of other private fields via reflection
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(jsonReader));

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    assertEquals(0, limitField.getInt(jsonReader));

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    assertEquals(1, lineNumberField.getInt(jsonReader)); // fixed back to 1 as per actual initial value

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    assertEquals(0, lineStartField.getInt(jsonReader));

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(0, peekedField.getInt(jsonReader)); // PEEKED_NONE = 0

    var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    assertEquals(0, stackSizeField.getInt(jsonReader));
  }
}