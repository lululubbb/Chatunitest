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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_506_5Test {

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

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNonNullValue_AddsToJsonObject() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, "name");
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(false);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        assertNull(pendingNameField.get(jsonTreeWriter));
        assertTrue(jsonObject.has("name"));
        assertEquals(value, jsonObject.get("name"));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValue_SerializeNullsTrue_AddsToJsonObject() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, "name");
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        // Use spy to override getSerializeNulls() to return true via reflection
        JsonTreeWriter spyWriter = Mockito.spy(jsonTreeWriter);

        // Instead of reflection for getSerializeNulls(), just doReturn(true)
        doReturn(true).when(spyWriter).getSerializeNulls();

        // Act
        putMethod.invoke(spyWriter, value);

        // Assert
        assertNull(pendingNameField.get(spyWriter));
        assertTrue(jsonObject.has("name"));
        assertEquals(value, jsonObject.get("name"));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValue_SerializeNullsFalse_DoesNotAddToJsonObject() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, "name");
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        // Use spy to override getSerializeNulls() to return false via reflection
        JsonTreeWriter spyWriter = Mockito.spy(jsonTreeWriter);

        // Instead of reflection for getSerializeNulls(), just doReturn(false)
        doReturn(false).when(spyWriter).getSerializeNulls();

        // Act
        putMethod.invoke(spyWriter, value);

        // Assert
        assertNull(pendingNameField.get(spyWriter));
        assertFalse(jsonObject.has("name"));
    }

    @Test
    @Timeout(8000)
    public void testPut_StackEmpty_SetsProduct() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);
        List<JsonElement> stack = new ArrayList<>();
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        assertEquals(value, productField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_StackNotEmptyAndTopIsJsonArray_AddsToJsonArray() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);
        JsonArray jsonArray = new JsonArray();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonArray);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        assertEquals(1, jsonArray.size());
        assertEquals(value, jsonArray.get(0));
    }

    @Test
    @Timeout(8000)
    public void testPut_StackNotEmptyAndTopIsNotJsonArray_ThrowsException() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);
        JsonObject jsonObject = new JsonObject();
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> putMethod.invoke(jsonTreeWriter, value));
        // Since invoke wraps exceptions in InvocationTargetException, check cause
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
    }
}