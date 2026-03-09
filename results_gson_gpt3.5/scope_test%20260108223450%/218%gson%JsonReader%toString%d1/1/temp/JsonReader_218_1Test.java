package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonReader_218_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader reader = new StringReader("");
    jsonReader = new JsonReader(reader);
  }

  @Test
    @Timeout(8000)
  void toString_includesClassNameAndLocationString() throws Exception {
    // Use reflection to invoke private locationString method
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(jsonReader);

    String toStringResult = jsonReader.toString();

    assertTrue(toStringResult.startsWith("JsonReader"));
    assertTrue(toStringResult.endsWith(locationString));
  }

  @Test
    @Timeout(8000)
  void locationString_reflectsLineNumberAndPosition() throws Exception {
    // Initially lineNumber=0, lineStart=0, pos=0, so locationString should be " at line 1 column 1 path $"
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonReader);
    assertEquals(" at line 1 column 1 path $", location);

    // Change lineNumber, lineStart, pos fields via reflection to simulate reading progress
    setField(jsonReader, "lineNumber", 3);
    setField(jsonReader, "lineStart", 15);
    setField(jsonReader, "pos", 20);
    setField(jsonReader, "stackSize", 1);

    // Ensure pathNames and pathIndices arrays are initialized and assigned properly
    String[] pathNames = new String[32];
    pathNames[0] = "name";
    setField(jsonReader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    setField(jsonReader, "pathIndices", pathIndices);

    // Also set stack[0] to a value indicating we are inside an object so path is computed correctly
    int[] stack = new int[32];

    // Get private static final field PEEKED_BEGIN_OBJECT via reflection
    Field peekedBeginObjectField = JsonReader.class.getDeclaredField("PEEKED_BEGIN_OBJECT");
    peekedBeginObjectField.setAccessible(true);
    int peekedBeginObject = peekedBeginObjectField.getInt(null);

    stack[0] = peekedBeginObject; // 1 means inside object
    setField(jsonReader, "stack", stack);

    location = (String) locationStringMethod.invoke(jsonReader);
    // lineNumber=3 means line 4, column = pos - lineStart + 1 = 20 - 15 + 1 = 6
    // pathNames[0] = "name", pathIndices[0] = 5, so path should be "$.name[5]"
    assertEquals(" at line 4 column 6 path $.name[5]", location);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}