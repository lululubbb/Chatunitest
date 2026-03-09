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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_219_4Test {

  private JsonReader jsonReader;
  private Method locationStringMethod;

  @BeforeEach
  void setUp() throws Exception {
    // Create a Reader stub since JsonReader requires a Reader in constructor
    Reader readerStub = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF immediately
      }
      @Override
      public void close() {
      }
    };
    jsonReader = new JsonReader(readerStub);

    // Access private method locationString via reflection
    locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testLocationString_basic() throws Exception {
    // Set lineNumber and lineStart fields via reflection
    setField("lineNumber", 4);
    setField("pos", 10);
    setField("lineStart", 5);

    // Set pathNames and pathIndices to simulate a path, and stackSize
    setField("stackSize", 2);
    setField("pathNames", new String[]{"", "myKey"});
    setField("pathIndices", new int[]{0, 3});

    // Also set stack to represent the JSON scopes for proper path construction
    // Use reflection to get the private static final int fields PEEKED_BEGIN_ARRAY and PEEKED_BEGIN_OBJECT
    int beginArray = getStaticFinalIntField("PEEKED_BEGIN_ARRAY");
    int beginObject = getStaticFinalIntField("PEEKED_BEGIN_OBJECT");

    setField("stack", new int[]{beginArray, beginObject});

    // Invoke locationString
    String result = (String) locationStringMethod.invoke(jsonReader);

    // Expected line = lineNumber + 1 = 5
    // column = pos - lineStart + 1 = 10 - 5 + 1 = 6
    // path = getPath() returns "$[3].myKey"
    assertEquals(" at line 5 column 6 path $[3].myKey", result);
  }

  @Test
    @Timeout(8000)
  void testLocationString_defaultPath() throws Exception {
    // No pathNames or pathIndices set, stackSize 0
    setField("lineNumber", 0);
    setField("pos", 0);
    setField("lineStart", 0);

    setField("stackSize", 0);
    setField("pathNames", new String[32]);
    setField("pathIndices", new int[32]);

    String result = (String) locationStringMethod.invoke(jsonReader);

    // line=1, column=1, path="$"
    assertEquals(" at line 1 column 1 path $", result);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private int getStaticFinalIntField(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(null);
  }
}