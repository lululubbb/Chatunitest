package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeWriter_503_4Test {

    private JsonTreeWriter writer;

    @BeforeEach
    void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    void testConstructor_initialState() throws Exception {
        // After construction, product should be JsonNull.INSTANCE
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonElement product = (JsonElement) productField.get(writer);
        assertEquals(JsonNull.INSTANCE, product);

        // stack should be empty list
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<?> stack = (List<?>) stackField.get(writer);
        assertNotNull(stack);
        assertTrue(stack.isEmpty());

        // pendingName should be null
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        Object pendingName = pendingNameField.get(writer);
        assertNull(pendingName);
    }

    @Test
    @Timeout(8000)
    void testGet_returnsProduct() throws Exception {
        // Initially product is JsonNull.INSTANCE
        Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
        getMethod.setAccessible(true);
        JsonElement result = (JsonElement) getMethod.invoke(writer);
        assertEquals(JsonNull.INSTANCE, result);

        // Put a JsonPrimitive to product field and verify get returns it
        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonPrimitive primitive = new JsonPrimitive("test");
        productField.set(writer, primitive);

        JsonElement result2 = (JsonElement) getMethod.invoke(writer);
        assertEquals(primitive, result2);
    }

    @Test
    @Timeout(8000)
    void testPeek_returnsTopOfStack() throws Exception {
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        // Initially stack is empty, peek should throw IndexOutOfBoundsException or similar
        Exception exception = assertThrows(Exception.class, () -> peekMethod.invoke(writer));
        // The cause should be IndexOutOfBoundsException
        assertTrue(exception.getCause() instanceof IndexOutOfBoundsException);

        // Add element to stack and peek should return it
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        JsonPrimitive element = new JsonPrimitive("element");
        stack.add(element);

        JsonElement peeked = (JsonElement) peekMethod.invoke(writer);
        assertEquals(element, peeked);
    }

    @Test
    @Timeout(8000)
    void testPut_addsElementToStackOrObject() throws Exception {
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);

        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);

        JsonPrimitive primitive = new JsonPrimitive("value");

        // Case 1: stack empty, put sets product
        putMethod.invoke(writer, primitive);
        JsonElement product = (JsonElement) productField.get(writer);
        assertEquals(primitive, product);

        // Reset product and stack
        productField.set(writer, JsonNull.INSTANCE);
        stack.clear();

        // Case 2: stack top is JsonArray, put adds element to array
        JsonArray array = new JsonArray();
        stack.add(array);
        putMethod.invoke(writer, primitive);
        assertEquals(1, array.size());
        assertEquals(primitive, array.get(0));

        // Case 3: stack top is JsonObject and pendingName is set, put adds property
        stack.clear();
        JsonObject object = new JsonObject();
        stack.add(object);
        pendingNameField.set(writer, "key");
        putMethod.invoke(writer, primitive);
        assertEquals(primitive, object.get("key"));
        assertNull(pendingNameField.get(writer));

        // Case 4: stack top is JsonObject but pendingName is null - should throw IllegalStateException
        stack.clear();
        stack.add(new JsonObject());
        pendingNameField.set(writer, null);
        Exception ex = assertThrows(Exception.class, () -> putMethod.invoke(writer, primitive));
        assertTrue(ex.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    void testBeginArray_andEndArray() {
        JsonWriter returned = writer.beginArray();
        assertSame(writer, returned);

        // After beginArray, stack top should be JsonArray
        JsonElement top = getStackTopElement();
        assertTrue(top instanceof JsonArray);

        // End array should close the array and pop it from stack
        returned = writer.endArray();
        assertSame(writer, returned);

        // After endArray, stack should be empty
        assertTrue(getStack().isEmpty());
    }

    @Test
    @Timeout(8000)
    void testBeginObject_andEndObject() {
        JsonWriter returned = writer.beginObject();
        assertSame(writer, returned);

        // After beginObject, stack top should be JsonObject
        JsonElement top = getStackTopElement();
        assertTrue(top instanceof JsonObject);

        // End object should close the object and pop it from stack
        returned = writer.endObject();
        assertSame(writer, returned);

        // After endObject, stack should be empty
        assertTrue(getStack().isEmpty());
    }

    @Test
    @Timeout(8000)
    void testName_setsPendingName() throws Exception {
        writer.beginObject();
        JsonWriter returned = writer.name("testName");
        assertSame(writer, returned);

        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        String pendingName = (String) pendingNameField.get(writer);
        assertEquals("testName", pendingName);
    }

    @Test
    @Timeout(8000)
    void testValueString_andNullValue() {
        JsonWriter returned = writer.value("stringValue");
        assertSame(writer, returned);
        JsonElement product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals("stringValue", product.getAsString());

        returned = writer.nullValue();
        assertSame(writer, returned);
        product = getProduct();
        assertTrue(product.isJsonNull());
    }

    @Test
    @Timeout(8000)
    void testValueBoolean_andValueBooleanObject() {
        JsonWriter returned = writer.value(true);
        assertSame(writer, returned);
        JsonElement product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertTrue(product.getAsBoolean());

        returned = writer.value(Boolean.FALSE);
        assertSame(writer, returned);
        product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertFalse(product.getAsBoolean());

        // Test null Boolean throws NullPointerException
        assertThrows(NullPointerException.class, () -> writer.value((Boolean) null));
    }

    @Test
    @Timeout(8000)
    void testValueFloat_double_long_Number() {
        JsonWriter returned;

        returned = writer.value(1.23f);
        assertSame(writer, returned);
        JsonElement product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals(1.23f, product.getAsFloat(), 0.0001);

        returned = writer.value(4.56d);
        assertSame(writer, returned);
        product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals(4.56d, product.getAsDouble(), 0.0001);

        returned = writer.value(123L);
        assertSame(writer, returned);
        product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals(123L, product.getAsLong());

        returned = writer.value((Number) 789);
        assertSame(writer, returned);
        product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals(789, product.getAsInt());

        // Test null Number throws NullPointerException
        assertThrows(NullPointerException.class, () -> writer.value((Number) null));
    }

    @Test
    @Timeout(8000)
    void testJsonValue_validJson() {
        JsonWriter returned = writer.jsonValue("\"jsonValue\"");
        assertSame(writer, returned);
        JsonElement product = getProduct();
        assertTrue(product.isJsonPrimitive());
        assertEquals("jsonValue", product.getAsString());
    }

    @Test
    @Timeout(8000)
    void testJsonValue_invalidJson_throwsIOException() {
        assertThrows(IOException.class, () -> writer.jsonValue("invalid json"));
    }

    @Test
    @Timeout(8000)
    void testFlush_doesNothing() {
        assertDoesNotThrow(() -> writer.flush());
    }

    @Test
    @Timeout(8000)
    void testClose_setsProductToSentinelClosed() throws Exception {
        writer.beginArray();
        writer.close();

        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonElement product = (JsonElement) productField.get(writer);

        Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelField.setAccessible(true);
        JsonPrimitive sentinel = (JsonPrimitive) sentinelField.get(null);

        assertEquals(sentinel, product);

        // Further calls to beginArray or name should throw IllegalStateException
        assertThrows(IllegalStateException.class, () -> writer.beginArray());
        assertThrows(IllegalStateException.class, () -> writer.name("name"));
    }

    // Helper methods to access private fields for verification
    private List<JsonElement> getStack() {
        try {
            Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
            return stack;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JsonElement getStackTopElement() {
        List<JsonElement> stack = getStack();
        if (stack.isEmpty()) {
            return null;
        }
        return stack.get(stack.size() - 1);
    }

    private JsonElement getProduct() {
        try {
            Field productField = JsonTreeWriter.class.getDeclaredField("product");
            productField.setAccessible(true);
            return (JsonElement) productField.get(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}