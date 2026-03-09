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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_228_3Test {

  private JsonPrimitive jsonPrimitive;
  private JsonObject jsonObject;
  private JsonArray jsonArray;
  private JsonNull jsonNull;

  @BeforeEach
  public void setUp() {
    jsonPrimitive = new JsonPrimitive("test");
    jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    jsonNull = JsonNull.INSTANCE;
  }

  @Test
    @Timeout(8000)
  public void testConstructor_pushesElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    // Access private field stack and stackSize to verify push
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(1, stackSize);
    assertSame(jsonPrimitive, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testPush_privateMethod() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    JsonPrimitive newPrimitive = new JsonPrimitive("new");
    pushMethod.invoke(reader, newPrimitive);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(2, stackSize);
    assertSame(newPrimitive, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_privateMethod() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    Object top = peekStackMethod.invoke(reader);
    assertSame(jsonPrimitive, top);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_privateMethod() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    Object popped = popStackMethod.invoke(reader);

    assertSame(jsonPrimitive, popped);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_andGetPreviousPath_privateMethod() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String pathFalse = (String) getPathMethod.invoke(reader, false);
    String pathTrue = (String) getPathMethod.invoke(reader, true);

    assertNotNull(pathFalse);
    assertNotNull(pathTrue);

    String previousPath = reader.getPreviousPath();
    assertNotNull(previousPath);

    String path = reader.getPath();
    assertNotNull(path);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_privateMethod() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    String location = (String) locationStringMethod.invoke(reader);
    assertNotNull(location);
    assertTrue(location.contains(" at path "));
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(1, stackSize);
    assertSame(jsonObject, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(1, stackSize);
    assertSame(jsonArray, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonNull);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(1, stackSize);
    assertSame(jsonNull, stack[stackSize - 1]);
  }
}