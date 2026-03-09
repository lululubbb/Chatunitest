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

public class JsonTreeWriter_506_2Test {

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
        JsonObject jsonObject = Mockito.mock(JsonObject.class);
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);
        pendingNameField.set(jsonTreeWriter, "name");

        JsonElement value = Mockito.mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(false);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        verify(jsonObject).add("name", value);
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValueAndSerializeNullsTrue_AddsToJsonObject() throws Exception {
        // Arrange
        JsonObject jsonObject = Mockito.mock(JsonObject.class);
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);

        // Spy on jsonTreeWriter to mock getSerializeNulls()
        JsonTreeWriter spyWriter = Mockito.spy(new JsonTreeWriter());
        pendingNameField.set(spyWriter, "name");
        stackField.set(spyWriter, stack);
        when(spyWriter.getSerializeNulls()).thenReturn(true);

        JsonElement value = Mockito.mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        // Access put method on spy
        Method putMethodSpy = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethodSpy.setAccessible(true);

        // Act
        putMethodSpy.invoke(spyWriter, value);

        // Assert
        verify(jsonObject).add("name", value);
        assertNull(pendingNameField.get(spyWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithPendingNameAndNullValueAndSerializeNullsFalse_DoesNotAdd() throws Exception {
        // Arrange
        JsonObject jsonObject = Mockito.mock(JsonObject.class);
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonObject);

        // Spy on jsonTreeWriter to mock getSerializeNulls()
        JsonTreeWriter spyWriter = Mockito.spy(new JsonTreeWriter());
        pendingNameField.set(spyWriter, "name");
        stackField.set(spyWriter, stack);
        when(spyWriter.getSerializeNulls()).thenReturn(false);

        JsonElement value = Mockito.mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        // Access put method on spy
        Method putMethodSpy = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethodSpy.setAccessible(true);

        // Act
        putMethodSpy.invoke(spyWriter, value);

        // Assert
        verify(jsonObject, never()).add(anyString(), any());
        assertNull(pendingNameField.get(spyWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithEmptyStack_SetsProduct() throws Exception {
        // Arrange
        List<JsonElement> stack = new ArrayList<>();
        stackField.set(jsonTreeWriter, stack);
        pendingNameField.set(jsonTreeWriter, null);

        JsonElement value = Mockito.mock(JsonElement.class);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        assertSame(value, productField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_WithStackContainingJsonArray_AddsValueToArray() throws Exception {
        // Arrange
        JsonArray jsonArray = Mockito.mock(JsonArray.class);
        List<JsonElement> stack = new ArrayList<>();
        stack.add(jsonArray);
        stackField.set(jsonTreeWriter, stack);
        pendingNameField.set(jsonTreeWriter, null);

        JsonElement value = Mockito.mock(JsonElement.class);

        // Act
        putMethod.invoke(jsonTreeWriter, value);

        // Assert
        verify(jsonArray).add(value);
    }

    @Test
    @Timeout(8000)
    public void testPut_WithStackContainingNonJsonArrayAndNoPendingName_ThrowsIllegalStateException() throws Exception {
        // Arrange
        JsonElement element = new JsonPrimitive("not an array");
        List<JsonElement> stack = new ArrayList<>();
        stack.add(element);
        stackField.set(jsonTreeWriter, stack);
        pendingNameField.set(jsonTreeWriter, null);

        JsonElement value = Mockito.mock(JsonElement.class);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            try {
                putMethod.invoke(jsonTreeWriter, value);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the cause and throw it to be caught by assertThrows
                throw e.getCause();
            }
        });
        // No further verification needed, exception is expected
    }
}