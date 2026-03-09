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
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_228_6Test {

  private JsonPrimitive jsonPrimitive;
  private JsonObject jsonObject;
  private JsonArray jsonArray;
  private JsonNull jsonNull;

  @BeforeEach
  public void setUp() {
    jsonPrimitive = new JsonPrimitive("value");
    jsonObject = new JsonObject();
    jsonObject.add("key", jsonPrimitive);
    jsonArray = new JsonArray();
    jsonArray.add(jsonPrimitive);
    jsonNull = JsonNull.INSTANCE;
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonPrimitive_shouldPushElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    // Use reflection to access private stack and stackSize
    Object[] stack = (Object[]) getPrivateField(reader, "stack");
    int stackSize = (int) getPrivateField(reader, "stackSize");

    assertEquals(1, stackSize);
    assertSame(jsonPrimitive, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonObject_shouldPushElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    Object[] stack = (Object[]) getPrivateField(reader, "stack");
    int stackSize = (int) getPrivateField(reader, "stackSize");

    assertEquals(1, stackSize);
    assertSame(jsonObject, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonArray_shouldPushElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    Object[] stack = (Object[]) getPrivateField(reader, "stack");
    int stackSize = (int) getPrivateField(reader, "stackSize");

    assertEquals(1, stackSize);
    assertSame(jsonArray, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withJsonNull_shouldPushElement() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(jsonNull);

    Object[] stack = (Object[]) getPrivateField(reader, "stack");
    int stackSize = (int) getPrivateField(reader, "stackSize");

    assertEquals(1, stackSize);
    assertSame(jsonNull, stack[stackSize - 1]);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  private Object invokePrivateMethod(Object instance, String methodName, Class<?>[] paramTypes, Object... params) throws Exception {
    Method method = instance.getClass().getDeclaredMethod(methodName, paramTypes);
    method.setAccessible(true);
    try {
      return method.invoke(instance, params);
    } catch (InvocationTargetException e) {
      throw (Exception) e.getCause();
    }
  }
}