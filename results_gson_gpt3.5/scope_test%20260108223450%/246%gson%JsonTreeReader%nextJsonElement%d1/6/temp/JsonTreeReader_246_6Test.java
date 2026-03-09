package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonTreeReader_246_6Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Use a JsonElement as constructor argument; can be any valid JsonElement
    com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
    jsonObject.addProperty("key", "value");
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_withValidJsonElement_returnsElement() throws Throwable {
    // We need to prepare the state so that peek() returns a valid JsonToken
    // and peekStack() returns a JsonElement.
    // Because peek() and peekStack() are private/protected, use reflection.

    Method peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    Method skipValueMethod = JsonTreeReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    // Confirm peek() returns a valid JsonToken not in the exception list:
    // JsonToken.NAME, END_ARRAY, END_OBJECT, END_DOCUMENT
    // We pick JsonToken.BEGIN_OBJECT for this test
    JsonToken peeked = JsonToken.BEGIN_OBJECT;

    // We cannot directly mock peek() because it's a real method.
    // Instead, we spy on jsonTreeReader and stub peek() to return peeked.
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(peeked).when(spyReader).peek();

    // Prepare peekStack() to return a JsonElement by manipulating the stack directly,
    // since peekStack() is private and cannot be stubbed by Mockito.

    // Use reflection to access the private 'stack' and 'stackSize' fields
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Push a JsonPrimitive onto the stack
    JsonPrimitive jsonPrimitive = new JsonPrimitive("primitive");
    stack[0] = jsonPrimitive;
    stackSizeField.setInt(spyReader, 1);

    // skipValue() is void, doNothing is default, so no need to stub.

    // Invoke nextJsonElement reflectively
    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    try {
      JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);
      assertNotNull(result);
      assertEquals(jsonPrimitive, result);
    } catch (InvocationTargetException e) {
      // unwrap invocation target exception
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_withNameToken_throwsIllegalStateException() throws Throwable {
    testNextJsonElementThrowsForToken(JsonToken.NAME);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_withEndArrayToken_throwsIllegalStateException() throws Throwable {
    testNextJsonElementThrowsForToken(JsonToken.END_ARRAY);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_withEndObjectToken_throwsIllegalStateException() throws Throwable {
    testNextJsonElementThrowsForToken(JsonToken.END_OBJECT);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_withEndDocumentToken_throwsIllegalStateException() throws Throwable {
    testNextJsonElementThrowsForToken(JsonToken.END_DOCUMENT);
  }

  private void testNextJsonElementThrowsForToken(JsonToken token) throws Throwable {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(token).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    try {
      nextJsonElementMethod.invoke(spyReader);
      fail("Expected IllegalStateException for token: " + token);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof IllegalStateException);
      assertEquals("Unexpected " + token + " when reading a JsonElement.", cause.getMessage());
    }
  }
}