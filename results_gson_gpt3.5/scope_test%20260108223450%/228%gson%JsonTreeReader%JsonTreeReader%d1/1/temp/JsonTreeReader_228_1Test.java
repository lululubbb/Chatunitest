package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_228_1Test {

  @Test
    @Timeout(8000)
  void constructor_shouldInitializeStackWithGivenElement() throws Exception {
    JsonPrimitive element = new JsonPrimitive("test");
    JsonTreeReader reader = new JsonTreeReader(element);

    // Using reflection to check private fields stack and stackSize
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(1, stackSize, "Stack size should be 1 after constructor");
    assertSame(element, stack[stackSize - 1], "Top of stack should be the element passed in constructor");
  }
}