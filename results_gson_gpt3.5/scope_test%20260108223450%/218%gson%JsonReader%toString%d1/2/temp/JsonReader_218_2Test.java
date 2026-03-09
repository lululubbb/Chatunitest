package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonReader_218_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = Mockito.mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void testToString_includesClassNameAndLocationString() throws Exception {
    // Use reflection to set private fields that affect locationString output if needed
    // locationString is package-private, so we can invoke it directly via reflection

    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    String locationString = (String) locationStringMethod.invoke(jsonReader);
    String expected = JsonReader.class.getSimpleName() + locationString;

    String actual = jsonReader.toString();

    assertEquals(expected, actual);
  }
}