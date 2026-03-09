package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_503_3Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initialState() throws Exception {
    // product field should be JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertSame(JsonNull.INSTANCE, product);

    // stack field should be empty list
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    assertNotNull(stack);
    assertTrue(stack.isEmpty());

    // pendingName should be null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    String pendingName = (String) pendingNameField.get(writer);
    assertNull(pendingName);
  }

  @Test
    @Timeout(8000)
  public void testGet_returnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement product = (JsonElement) getMethod.invoke(writer);
    assertSame(JsonNull.INSTANCE, product);

    // Use beginObject and endObject to create a JsonObject product
    writer.beginObject().endObject();
    JsonElement productAfter = (JsonElement) getMethod.invoke(writer);
    assertTrue(productAfter instanceof JsonObject);
  }

  @Test
    @Timeout(8000)
  public void testPeek_returnsTopOfStack() throws Exception {
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Initially stack is empty, so peek should throw IndexOutOfBoundsException
    assertThrows(IndexOutOfBoundsException.class, () -> peekMethod.invoke(writer));

    // Push a JsonObject manually and peek should return it
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    JsonObject obj = new JsonObject();
    stack.add(obj);

    JsonElement peeked = (JsonElement) peekMethod.invoke(writer);
    assertSame(obj, peeked);
  }

  @Test
    @Timeout(8000)
  public void testPut_addsElementCorrectly() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // put on empty stack sets product
    JsonPrimitive prim = new JsonPrimitive("test");
    putMethod.invoke(writer, prim);
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertSame(prim, product);

    // Clear product and stack for next test
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();
    productField.set(writer, JsonNull.INSTANCE);

    // Add JsonObject to stack, set pendingName, then put JsonPrimitive to object
    JsonObject obj = new JsonObject();
    stack.add(obj);
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, "key");

    putMethod.invoke(writer, prim);
    assertEquals(prim, obj.get("key"));
    assertNull(pendingNameField.get(writer));

    // put into JsonArray on top of stack
    JsonArray arr = new JsonArray();
    stack.clear();
    stack.add(arr);
    putMethod.invoke(writer, prim);
    assertEquals(prim, arr.get(0));
  }

  @Test
    @Timeout(8000)
  public void testBeginArray_and_EndArray() {
    JsonWriter returned = writer.beginArray();
    assertSame(writer, returned);

    // After beginArray, stack top should be JsonArray
    List<JsonElement> stack = getStack(writer);
    assertFalse(stack.isEmpty());
    assertTrue(stack.get(stack.size() - 1) instanceof JsonArray);

    // End array should pop the JsonArray and set as product if stack empty
    returned = writer.endArray();
    assertSame(writer, returned);

    stack = getStack(writer);
    assertTrue(stack.isEmpty());

    JsonElement product = getProduct(writer);
    assertTrue(product instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  public void testBeginObject_and_EndObject() {
    JsonWriter returned = writer.beginObject();
    assertSame(writer, returned);

    List<JsonElement> stack = getStack(writer);
    assertFalse(stack.isEmpty());
    assertTrue(stack.get(stack.size() - 1) instanceof JsonObject);

    returned = writer.endObject();
    assertSame(writer, returned);

    stack = getStack(writer);
    assertTrue(stack.isEmpty());

    JsonElement product = getProduct(writer);
    assertTrue(product instanceof JsonObject);
  }

  @Test
    @Timeout(8000)
  public void testName_setsPendingName() {
    writer.beginObject();
    JsonWriter returned = writer.name("foo");
    assertSame(writer, returned);

    String pendingName = getPendingName(writer);
    assertEquals("foo", pendingName);
  }

  @Test
    @Timeout(8000)
  public void testValue_String() {
    writer.beginArray();
    JsonWriter returned = writer.value("hello");
    assertSame(writer, returned);

    JsonElement product = getProduct(writer);
    // product should still be JsonNull because array not ended
    assertEquals(JsonNull.INSTANCE, product);

    // End array to check value was added
    writer.endArray();
    product = getProduct(writer);
    assertTrue(product instanceof JsonArray);
    assertEquals("hello", ((JsonArray) product).get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testJsonValue_validJson() throws IOException {
    writer.beginArray();
    JsonWriter returned = writer.jsonValue("123");
    assertSame(writer, returned);
    writer.endArray();
    JsonElement product = getProduct(writer);
    assertTrue(product instanceof JsonArray);
    assertEquals(123, ((JsonArray) product).get(0).getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testJsonValue_invalidJson() {
    writer.beginArray();
    assertThrows(IOException.class, () -> writer.jsonValue("invalid json"));
  }

  @Test
    @Timeout(8000)
  public void testNullValue() {
    writer.beginArray();
    JsonWriter returned = writer.nullValue();
    assertSame(writer, returned);
    writer.endArray();
    JsonElement product = getProduct(writer);
    assertTrue(product instanceof JsonArray);
    assertTrue(((JsonArray) product).get(0).isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testValue_boolean() {
    writer.beginArray();
    writer.value(true);
    writer.value(false);
    writer.endArray();
    JsonElement product = getProduct(writer);
    JsonArray arr = (JsonArray) product;
    assertTrue(arr.get(0).getAsBoolean());
    assertFalse(arr.get(1).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValue_Boolean_object() {
    writer.beginArray();
    writer.value(Boolean.TRUE);
    writer.value(Boolean.FALSE);
    writer.value((Boolean) null);
    writer.endArray();
    JsonArray arr = (JsonArray) getProduct(writer);
    assertTrue(arr.get(0).getAsBoolean());
    assertFalse(arr.get(1).getAsBoolean());
    assertTrue(arr.get(2).isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testValue_float_double_long_Number() {
    writer.beginArray();
    writer.value(1.23f);
    writer.value(4.56d);
    writer.value(789L);
    writer.value((Number) new Integer(10));
    writer.endArray();
    JsonArray arr = (JsonArray) getProduct(writer);
    assertEquals(1.23f, arr.get(0).getAsFloat());
    assertEquals(4.56d, arr.get(1).getAsDouble());
    assertEquals(789L, arr.get(2).getAsLong());
    assertEquals(10, arr.get(3).getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testFlush_noOp() {
    assertDoesNotThrow(() -> writer.flush());
  }

  @Test
    @Timeout(8000)
  public void testClose_marksClosed() throws Exception {
    writer.close();

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);

    Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    JsonPrimitive sentinel = (JsonPrimitive) sentinelField.get(null);

    assertSame(sentinel, product);

    // After close, beginArray should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> writer.beginArray());
  }

  private List<JsonElement> getStack(JsonTreeWriter w) {
    try {
      Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      return (List<JsonElement>) stackField.get(w);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private JsonElement getProduct(JsonTreeWriter w) {
    try {
      Field productField = JsonTreeWriter.class.getDeclaredField("product");
      productField.setAccessible(true);
      return (JsonElement) productField.get(w);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String getPendingName(JsonTreeWriter w) {
    try {
      Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
      pendingNameField.setAccessible(true);
      return (String) pendingNameField.get(w);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}