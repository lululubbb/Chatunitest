package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeWriter_516_4Test {

  JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
    // Initialize the writer state by beginning an array to allow putting values
    try {
      jsonTreeWriter.beginArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void value_withNull_callsNullValueAndReturnsSame() throws IOException {
    JsonWriter returned = jsonTreeWriter.value((Boolean) null);
    assertSame(jsonTreeWriter, returned);
  }

  @Test
    @Timeout(8000)
  void value_withTrue_putCalledWithJsonPrimitiveTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonWriter returned = jsonTreeWriter.value(Boolean.TRUE);
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private peek method to verify the JsonPrimitive value was added
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement top = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    // The top element is a JsonArray, get its last element
    assertTrue(top instanceof JsonArray);
    JsonArray array = (JsonArray) top;
    JsonElement lastElement = array.get(array.size() - 1);

    assertTrue(lastElement instanceof JsonPrimitive);
    assertEquals(Boolean.TRUE, ((JsonPrimitive) lastElement).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void value_withFalse_putCalledWithJsonPrimitiveFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonWriter returned = jsonTreeWriter.value(Boolean.FALSE);
    assertSame(jsonTreeWriter, returned);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement top = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    // The top element is a JsonArray, get its last element
    assertTrue(top instanceof JsonArray);
    JsonArray array = (JsonArray) top;
    JsonElement lastElement = array.get(array.size() - 1);

    assertTrue(lastElement instanceof JsonPrimitive);
    assertEquals(Boolean.FALSE, ((JsonPrimitive) lastElement).getAsBoolean());
  }
}