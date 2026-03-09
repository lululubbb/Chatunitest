package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_503_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initialState() throws Exception {
    // Check that the stack is empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);
    assertNotNull(stack);
    assertTrue(stack.isEmpty());

    // Check that pendingName is null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    assertNull(pendingNameField.get(jsonTreeWriter));

    // Check that product is JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, product);

    // Check that the writer is the UNWRITABLE_WRITER
    Field writerField = JsonWriter.class.getDeclaredField("out");
    writerField.setAccessible(true);
    Writer writer = (Writer) writerField.get(jsonTreeWriter);
    assertNotNull(writer);
    // Attempting to write should throw AssertionError
    assertThrows(AssertionError.class, () -> writer.write("test"));
  }

  @Test
    @Timeout(8000)
  public void testUnwritableWriter_methodsThrowAssertionError() throws Exception {
    // Get the UNWRITABLE_WRITER instance via reflection
    Field writerField = JsonTreeWriter.class.getDeclaredField("UNWRITABLE_WRITER");
    writerField.setAccessible(true);
    Writer unwritableWriter = (Writer) writerField.get(null);

    // write(char[], int, int) throws AssertionError
    assertThrows(AssertionError.class, () -> unwritableWriter.write(new char[]{'a'}, 0, 1));

    // flush() throws AssertionError
    assertThrows(AssertionError.class, unwritableWriter::flush);

    // close() throws AssertionError
    assertThrows(AssertionError.class, unwritableWriter::close);
  }

  @Test
    @Timeout(8000)
  public void testSentinelClosed_isJsonPrimitiveWithValueClosed() throws Exception {
    Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    JsonPrimitive sentinel = (JsonPrimitive) sentinelField.get(null);
    assertEquals("closed", sentinel.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testPrivatePeekAndPutMethods() throws Exception {
    // Access private peek method
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Access private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially, peek should throw because stack is empty (likely IllegalStateException)
    Exception ex = assertThrows(Exception.class, () -> peekMethod.invoke(jsonTreeWriter));
    assertTrue(ex.getCause() instanceof IllegalStateException);

    // Use put to add a JsonNull element
    putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);

    // Now peek should return JsonNull.INSTANCE
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, peeked);
  }

  @Test
    @Timeout(8000)
  public void testBeginArrayAndEndArray() throws IOException {
    JsonWriter writer = jsonTreeWriter.beginArray();
    assertSame(jsonTreeWriter, writer);

    // The stack should contain one JsonArray element
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonArray);

    // Ending the array should remove the JsonArray and update product
    writer.endArray();
    assertTrue(stack.isEmpty());

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertTrue(product instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  public void testBeginObjectAndEndObject() throws IOException {
    JsonWriter writer = jsonTreeWriter.beginObject();
    assertSame(jsonTreeWriter, writer);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonObject);

    writer.endObject();
    assertTrue(stack.isEmpty());

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertTrue(product instanceof JsonObject);
  }

  @Test
    @Timeout(8000)
  public void testNameSetsPendingName() throws IOException {
    jsonTreeWriter.beginObject();
    JsonWriter writer = jsonTreeWriter.name("testName");
    assertSame(jsonTreeWriter, writer);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    String pendingName = (String) pendingNameField.get(jsonTreeWriter);
    assertEquals("testName", pendingName);
  }

  @Test
    @Timeout(8000)
  public void testValueString() throws IOException {
    jsonTreeWriter.beginArray();
    JsonWriter writer = jsonTreeWriter.value("stringValue");
    assertSame(jsonTreeWriter, writer);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertEquals(1, stack.size());
    JsonElement element = stack.get(0);
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertEquals("stringValue", array.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testNullValue() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.nullValue();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertEquals(1, stack.size());
    JsonArray array = (JsonArray) stack.get(0);
    assertEquals(1, array.size());
    assertTrue(array.get(0).isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testValueBoolean() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    JsonArray array = (JsonArray) stack.get(0);
    assertEquals(1, array.size());
    assertTrue(array.get(0).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValueNumber() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(123);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    JsonArray array = (JsonArray) stack.get(0);
    assertEquals(1, array.size());
    assertEquals(123, array.get(0).getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testCloseSetsProductToSentinelClosed() throws IOException, ReflectiveOperationException {
    jsonTreeWriter.close();

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(jsonTreeWriter);

    Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    Object sentinelClosed = sentinelField.get(null);

    assertSame(sentinelClosed, product);
  }

  @Test
    @Timeout(8000)
  public void testFlushDoesNothing() {
    // flush is overridden but does nothing, no exception
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
    @Timeout(8000)
  public void testGetReturnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement product = (JsonElement) getMethod.invoke(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, product);

    // After beginArray, product is still JsonNull until endArray
    jsonTreeWriter.beginArray();
    product = (JsonElement) getMethod.invoke(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, product);

    // After endArray, product is JsonArray
    jsonTreeWriter.endArray();
    product = (JsonElement) getMethod.invoke(jsonTreeWriter);
    assertTrue(product instanceof JsonArray);
  }
}