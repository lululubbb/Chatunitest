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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_506_1Test {

    private JsonTreeWriter jsonTreeWriter;
    private Method putMethod;
    private Field pendingNameField;
    private Field stackField;
    private Field productField;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, NoSuchFieldException {
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
    public void testPut_WithPendingNameAndNonNullValue_AddsToJsonObject() throws Throwable {
        // Arrange
        JsonObject mockObject = mock(JsonObject.class);
        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(false);

        List<JsonElement> stack = new ArrayList<>();
        stack.add(mockObject);

        pendingNameField.set(jsonTreeWriter, "name");
        stackField.set(jsonTreeWriter, stack);

        // Act
        invokePut(value);

        // Assert
        verify(mockObject).add("name", value);
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValue_SerializeNullsFalse_DoesNotAdd() throws Throwable {
        // Arrange
        JsonObject mockObject = mock(JsonObject.class);
        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        List<JsonElement> stack = new ArrayList<>();
        stack.add(mockObject);

        pendingNameField.set(jsonTreeWriter, "name");
        stackField.set(jsonTreeWriter, stack);

        // Use reflection to set serializeNulls to false via setter method
        setSerializeNulls(jsonTreeWriter, false);

        // Act
        invokePut(value);

        // Assert
        verify(mockObject, never()).add(anyString(), any());
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValue_SerializeNullsTrue_AddsToJsonObject() throws Throwable {
        // Arrange
        JsonObject mockObject = mock(JsonObject.class);
        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        List<JsonElement> stack = new ArrayList<>();
        stack.add(mockObject);

        pendingNameField.set(jsonTreeWriter, "name");
        stackField.set(jsonTreeWriter, stack);

        // Use reflection to set serializeNulls to true via setter method
        setSerializeNulls(jsonTreeWriter, true);

        // Act
        invokePut(value);

        // Assert
        verify(mockObject).add("name", value);
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_EmptyStack_SetsProduct() throws Throwable {
        // Arrange
        JsonElement value = mock(JsonElement.class);
        pendingNameField.set(jsonTreeWriter, null);
        stackField.set(jsonTreeWriter, new ArrayList<>());

        // Act
        invokePut(value);

        // Assert
        assertSame(value, productField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_StackWithJsonArray_AddsToArray() throws Throwable {
        // Arrange
        JsonArray mockArray = mock(JsonArray.class);
        JsonElement value = mock(JsonElement.class);

        List<JsonElement> stack = new ArrayList<>();
        stack.add(mockArray);

        pendingNameField.set(jsonTreeWriter, null);
        stackField.set(jsonTreeWriter, stack);

        // Act
        invokePut(value);

        // Assert
        verify(mockArray).add(value);
    }

    @Test
    @Timeout(8000)
    public void testPut_StackWithNonJsonArrayAndNoPendingName_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        JsonElement nonArrayElement = mock(JsonElement.class);

        List<JsonElement> stack = new ArrayList<>();
        stack.add(nonArrayElement);

        pendingNameField.set(jsonTreeWriter, null);
        stackField.set(jsonTreeWriter, stack);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokePut(mock(JsonElement.class)));
        assertNotNull(thrown);
    }

    private void invokePut(JsonElement value) throws Throwable {
        try {
            putMethod.invoke(jsonTreeWriter, value);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void setSerializeNulls(JsonTreeWriter writer, boolean value) throws Exception {
        // Try to find and invoke setSerializeNulls(boolean) method if exists
        try {
            Method setSerializeNullsMethod = JsonTreeWriter.class.getMethod("setSerializeNulls", boolean.class);
            setSerializeNullsMethod.invoke(writer, value);
            return;
        } catch (NoSuchMethodException ignored) {
            // Method not found, proceed to try field or fallback
        }

        // Try to find a getter method getSerializeNulls()
        try {
            Method getSerializeNullsMethod = JsonTreeWriter.class.getMethod("getSerializeNulls");
            // If getter exists but no setter, fail test because we cannot set it
            throw new UnsupportedOperationException("No setter for serializeNulls found");
        } catch (NoSuchMethodException ignored) {
            // No getter either, try to find a field with a different name or fallback
        }

        // Try to find a field named 'serializeNulls' or similar in superclass JsonWriter
        Class<?> clazz = JsonTreeWriter.class;
        Field serializeNullsField = null;
        while (clazz != null) {
            try {
                serializeNullsField = clazz.getDeclaredField("serializeNulls");
                serializeNullsField.setAccessible(true);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        if (serializeNullsField != null) {
            serializeNullsField.set(writer, value);
            return;
        }

        // If still not found, try to find field in JsonWriter superclass (which is public)
        clazz = JsonTreeWriter.class.getSuperclass();
        while (clazz != null) {
            try {
                serializeNullsField = clazz.getDeclaredField("serializeNulls");
                serializeNullsField.setAccessible(true);
                serializeNullsField.set(writer, value);
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field 'serializeNulls' not found in JsonTreeWriter or its superclasses");
    }
}