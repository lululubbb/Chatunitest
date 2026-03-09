package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_254_1Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Initialize with an empty JsonObject for default
    reader = new JsonTreeReader(new JsonObject());
    // Reset stackSize and stack to simulate initial state
    setPrivateField(reader, "stackSize", 0);
    setPrivateField(reader, "stack", new Object[32]);
    setPrivateField(reader, "pathNames", new String[32]);
    setPrivateField(reader, "pathIndices", new int[32]);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_emptyStack_returnsRoot() throws Exception {
    // stackSize is 0 to simulate empty stack
    setPrivateField(reader, "stackSize", 0);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(reader, false);
    assertEquals("$", path);

    String pathUsePrevious = (String) getPathMethod.invoke(reader, true);
    assertEquals("$", pathUsePrevious);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withObjectAndArray() throws Exception {
    /*
     * Setup stack and pathNames/pathIndices to simulate JSON like:
     * {
     *   "name": [
     *     1,
     *     2
     *   ]
     * }
     * 
     * stackSize = 3:
     * stack[0] = JsonObject
     * stack[1] = JsonArray
     * stack[2] = JsonPrimitive
     * 
     * pathNames[1] = "name"
     * pathIndices[2] = 1 (index in array)
     */

    JsonObject rootObject = new JsonObject();
    JsonArray array = new JsonArray();
    JsonPrimitive primitive = new JsonPrimitive(123);

    Object[] stack = new Object[32];
    stack[0] = rootObject;
    stack[1] = array;
    stack[2] = primitive;
    setPrivateField(reader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setPrivateField(reader, "pathNames", pathNames);

    int[] pathIndices = new int[32];
    pathIndices[2] = 1;
    setPrivateField(reader, "pathIndices", pathIndices);

    setPrivateField(reader, "stackSize", 3);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(reader, false);
    // Expected path: $.name[1]
    assertEquals("$.name[1]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withOnlyArrayIndexes() throws Exception {
    /*
     * Setup stackSize = 2 with two arrays, indexes 0 and 2:
     * path should be $[0][2]
     */
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    Object[] stack = new Object[32];
    stack[0] = array1;
    stack[1] = array2;
    setPrivateField(reader, "stack", stack);

    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 2;
    setPrivateField(reader, "pathIndices", pathIndices);

    // pathNames are null by default
    setPrivateField(reader, "pathNames", new String[32]);

    setPrivateField(reader, "stackSize", 2);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(reader, false);
    assertEquals("$[0][2]", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withNullPathNamesAndIndexes() throws Exception {
    /*
     * Setup stackSize = 2 with a JsonObject and JsonPrimitive, no pathNames or pathIndices set
     * The path should just include $ and then empty or no names/indexes
     */
    JsonObject obj = new JsonObject();
    JsonPrimitive prim = new JsonPrimitive("value");

    Object[] stack = new Object[32];
    stack[0] = obj;
    stack[1] = prim;
    setPrivateField(reader, "stack", stack);

    setPrivateField(reader, "pathNames", new String[32]);
    setPrivateField(reader, "pathIndices", new int[32]);

    setPrivateField(reader, "stackSize", 2);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(reader, false);
    // Since pathNames and pathIndices are not set, path should be just "$"
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_withUsePreviousPathTrue() throws Exception {
    /*
     * Test getPath(true) returns same as getPath(false) since no special logic in provided code snippet
     * Setup stackSize=1 with JsonObject and pathNames[0] = "obj"
     */
    JsonObject obj = new JsonObject();

    Object[] stack = new Object[32];
    stack[0] = obj;
    setPrivateField(reader, "stack", stack);

    String[] pathNames = new String[32];
    pathNames[0] = "obj";
    setPrivateField(reader, "pathNames", pathNames);

    setPrivateField(reader, "pathIndices", new int[32]);

    setPrivateField(reader, "stackSize", 1);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String pathFalse = (String) getPathMethod.invoke(reader, false);
    String pathTrue = (String) getPathMethod.invoke(reader, true);

    assertEquals("$.obj", pathFalse);
    assertEquals("$.obj", pathTrue);
  }

  private void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = getField(target.getClass(), fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}