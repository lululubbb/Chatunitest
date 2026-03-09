package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_228_4Test {

  private JsonPrimitive jsonPrimitive;
  private JsonObject jsonObject;
  private JsonArray jsonArray;
  private JsonNull jsonNull;

  @BeforeEach
  public void setUp() {
    jsonPrimitive = new JsonPrimitive("value");
    jsonObject = new JsonObject();
    jsonArray = new JsonArray();
    jsonNull = JsonNull.INSTANCE;
  }

  @Test
    @Timeout(8000)
  public void testConstructor_pushesElementOntoStack() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    // Use reflection to access private stack and stackSize
    Object[] stack = (Object[]) getPrivateField(reader, "stack");
    int stackSize = (int) getPrivateField(reader, "stackSize");

    assertEquals(1, stackSize);
    assertSame(jsonPrimitive, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testPushAndPopStack() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Push jsonObject
    pushMethod.invoke(reader, jsonObject);
    int stackSize = (int) getPrivateField(reader, "stackSize");
    assertEquals(2, stackSize);

    // Pop stack and verify it returns jsonObject
    Object popped = popStackMethod.invoke(reader);
    assertSame(jsonObject, popped);

    stackSize = (int) getPrivateField(reader, "stackSize");
    assertEquals(1, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testPeekStack() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    Object top = peekStackMethod.invoke(reader);
    assertSame(jsonPrimitive, top);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_initialAndAfterPush() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String initialPath = (String) getPathMethod.invoke(reader, true);
    assertEquals("$", initialPath);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(reader, jsonObject);

    String pathAfterPush = (String) getPathMethod.invoke(reader, true);
    assertEquals("$", pathAfterPush); // pathNames and pathIndices default to empty or 0, so path remains root
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath() {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    String prevPath = reader.getPreviousPath();
    assertEquals("$", prevPath);
  }

  @Test
    @Timeout(8000)
  public void testToString_containsClassName() {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    String toString = reader.toString();
    assertTrue(toString.contains(JsonTreeReader.class.getSimpleName()));
  }

  // Helper method to access private fields
  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}