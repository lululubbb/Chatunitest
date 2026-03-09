package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
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

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeReader_246_4Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonElement to initialize JsonTreeReader
    JsonElement element = new JsonPrimitive("test");
    reader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_unexpectedTokens_throwIllegalStateException() throws Exception {
    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    // Use reflection to mock peek() to return each unexpected token and test exception
    JsonToken[] unexpectedTokens = {
        JsonToken.NAME,
        JsonToken.END_ARRAY,
        JsonToken.END_OBJECT,
        JsonToken.END_DOCUMENT
    };

    for (JsonToken token : unexpectedTokens) {
      JsonTreeReader spyReader = spy(reader);
      doReturn(token).when(spyReader).peek();

      IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
        try {
          nextJsonElementMethod.invoke(spyReader);
        } catch (Exception e) {
          // Unwrap the cause thrown by reflection
          Throwable cause = e.getCause();
          if (cause instanceof IllegalStateException) {
            throw cause;
          } else {
            throw e;
          }
        }
      });
      assertTrue(thrown.getMessage().contains("Unexpected " + token));
    }
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_validToken_returnsElement() throws Exception {
    // Prepare a JsonPrimitive element and push it on the stack
    JsonPrimitive primitive = new JsonPrimitive("value");
    JsonTreeReader spyReader = spy(new JsonTreeReader(primitive));

    // Mock peek() to return a token not in the exception list (e.g. STRING)
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Instead of mocking private peekStack(), use reflection to set the stack field directly
    // so that peekStack() returns the expected primitive

    // Set private field 'stack' and 'stackSize' to simulate the internal state
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackField.set(spyReader, stackArray);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    // We also need to mock skipValue(), as it is called inside nextJsonElement
    doNothing().when(spyReader).skipValue();

    // Invoke nextJsonElement via reflection
    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    assertEquals(primitive, result);
    verify(spyReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_validToken_withJsonObject() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");

    JsonTreeReader spyReader = spy(new JsonTreeReader(jsonObject));

    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Set private field 'stack' and 'stackSize' to simulate the internal state
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = jsonObject;
    stackField.set(spyReader, stackArray);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    doNothing().when(spyReader).skipValue();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    assertEquals(jsonObject, result);
    verify(spyReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_validToken_withJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader spyReader = spy(new JsonTreeReader(jsonArray));

    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    // Set private field 'stack' and 'stackSize' to simulate the internal state
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = jsonArray;
    stackField.set(spyReader, stackArray);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    doNothing().when(spyReader).skipValue();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    assertEquals(jsonArray, result);
    verify(spyReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_validToken_withJsonNull() throws Exception {
    JsonNull jsonNull = JsonNull.INSTANCE;

    JsonTreeReader spyReader = spy(new JsonTreeReader(jsonNull));

    doReturn(JsonToken.NULL).when(spyReader).peek();

    // Set private field 'stack' and 'stackSize' to simulate the internal state
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = jsonNull;
    stackField.set(spyReader, stackArray);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    doNothing().when(spyReader).skipValue();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    assertEquals(jsonNull, result);
    verify(spyReader).skipValue();
  }
}