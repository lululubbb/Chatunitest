package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_506_4Test {

    private JsonTreeWriter jsonTreeWriter;
    private Method putMethod;
    private Field pendingNameField;
    private Field stackField;
    private Field productField;

    @BeforeEach
    public void setUp() throws Exception {
        jsonTreeWriter = new JsonTreeWriter();

        putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);

        stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
    }

    private void setPendingName(String name) throws IllegalAccessException {
        pendingNameField.set(jsonTreeWriter, name);
    }

    private String getPendingName() throws IllegalAccessException {
        return (String) pendingNameField.get(jsonTreeWriter);
    }

    @SuppressWarnings("unchecked")
    private List<JsonElement> getStack() throws IllegalAccessException {
        return (List<JsonElement>) stackField.get(jsonTreeWriter);
    }

    private JsonElement getProduct() throws IllegalAccessException {
        return (JsonElement) productField.get(jsonTreeWriter);
    }

    private void setProduct(JsonElement element) throws IllegalAccessException {
        productField.set(jsonTreeWriter, element);
    }

    private void setStack(List<JsonElement> stack) throws IllegalAccessException {
        stackField.set(jsonTreeWriter, stack);
    }

    private void addToStack(JsonElement element) throws IllegalAccessException {
        getStack().add(element);
    }

    private JsonElement callPut(JsonElement value) throws Exception {
        putMethod.invoke(jsonTreeWriter, value);
        return getProduct();
    }

    private JsonElement peek() throws Exception {
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        return (JsonElement) peekMethod.invoke(jsonTreeWriter);
    }

    // Helper class to wrap JsonTreeWriter and override getSerializeNulls via reflection
    private static class JsonTreeWriterWithSerializeNulls {
        private final JsonTreeWriter delegate;
        private final boolean serializeNulls;

        public JsonTreeWriterWithSerializeNulls(boolean serializeNulls) throws Exception {
            this.delegate = new JsonTreeWriter();
            this.serializeNulls = serializeNulls;

            // Override getSerializeNulls method via proxying is not possible since it's final.
            // Instead, we will override the field "serializeNulls" in superclass JsonWriter via reflection,
            // which is private final boolean serializeNulls;
            // But Gson's JsonWriter doesn't have a field named serializeNulls, getSerializeNulls() returns a final method that returns a private field.
            // So we can try to override the field "serializeNulls" in JsonWriter via reflection.

            // JsonWriter has a private boolean serializeNulls field, set it accordingly.
            Class<?> jsonWriterClass = JsonTreeWriter.class.getSuperclass();
            Field serializeNullsField = jsonWriterClass.getDeclaredField("serializeNulls");
            serializeNullsField.setAccessible(true);
            serializeNullsField.setBoolean(delegate, serializeNulls);
        }

        public JsonTreeWriter getDelegate() {
            return delegate;
        }
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingName_andValueNotJsonNull_serializeNullsTrue_addsToObject() throws Exception {
        // Use helper class to set serializeNulls to true
        JsonTreeWriterWithSerializeNulls wrapper = new JsonTreeWriterWithSerializeNulls(true);
        JsonTreeWriter writer = wrapper.getDelegate();

        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        // Set pendingName
        pendingNameField.set(writer, "key");

        // Prepare JsonObject on stack
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(writer, stack);

        // Get put method
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        JsonPrimitive value = new JsonPrimitive("value");

        putMethod.invoke(writer, value);

        // Verify value added to object
        assertTrue(jsonObject.has("key"));
        assertEquals(value, jsonObject.get("key"));
        // pendingName reset to null
        assertNull(pendingNameField.get(writer));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingName_andValueIsJsonNull_serializeNullsFalse_doesNotAddToObject() throws Exception {
        // Use helper class to set serializeNulls to false
        JsonTreeWriterWithSerializeNulls wrapper = new JsonTreeWriterWithSerializeNulls(false);
        JsonTreeWriter writer = wrapper.getDelegate();

        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        // Set pendingName
        pendingNameField.set(writer, "key");

        // Prepare JsonObject on stack
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(writer, stack);

        // Get put method
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        JsonNull value = JsonNull.INSTANCE;

        putMethod.invoke(writer, value);

        // Verify value NOT added to object
        assertFalse(jsonObject.has("key"));
        // pendingName reset to null
        assertNull(pendingNameField.get(writer));
    }

    @Test
    @Timeout(8000)
    public void testPut_pendingNameNull_stackEmpty_setsProduct() throws Exception {
        setPendingName(null);
        setStack(new ArrayList<>());

        JsonPrimitive value = new JsonPrimitive("productValue");

        callPut(value);

        // product should be set to value
        assertEquals(value, getProduct());
    }

    @Test
    @Timeout(8000)
    public void testPut_pendingNameNull_stackTopIsJsonArray_addsValueToArray() throws Exception {
        setPendingName(null);

        JsonArray jsonArray = new JsonArray();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonArray);
        setStack(stack);

        JsonPrimitive value = new JsonPrimitive("arrayValue");

        callPut(value);

        // JsonArray should contain the value
        assertEquals(1, jsonArray.size());
        assertEquals(value, jsonArray.get(0));
    }

    @Test
    @Timeout(8000)
    public void testPut_pendingNameNull_stackTopNotJsonArray_throwsIllegalStateException() throws Exception {
        setPendingName(null);

        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        setStack(stack);

        JsonPrimitive value = new JsonPrimitive("badValue");

        Exception exception = assertThrows(Exception.class, () -> callPut(value));
        // The cause should be IllegalStateException
        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalStateException);
    }
}