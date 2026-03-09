package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
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
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_506_3Test {

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

    private void invokePut(JsonElement value) throws InvocationTargetException, IllegalAccessException {
        try {
            putMethod.invoke(jsonTreeWriter, value);
        } catch (InvocationTargetException e) {
            // Unwrap IllegalStateException and rethrow for assertThrows
            if (e.getCause() instanceof IllegalStateException) {
                throw (IllegalStateException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNotNull_valueNotJsonNull_addsToJsonObject() throws Exception {
        // Arrange
        String name = "key";
        pendingNameField.set(jsonTreeWriter, name);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(false);

        JsonObject jsonObject = spy(new JsonObject());
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonObject);

        // Act
        invokePut(value);

        // Assert
        verify(jsonObject).add(name, value);
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNotNull_valueIsJsonNull_serializeNullsTrue_addsToJsonObject() throws Exception {
        // Arrange
        String name = "key";

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        JsonObject jsonObject = spy(new JsonObject());
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonObject);

        // Create a JsonTreeWriter instance with serializeNulls set to true via reflection
        JsonTreeWriter writerOverride = new JsonTreeWriter();

        // Set pendingName and stack on writerOverride
        Field stackFieldOverride = JsonTreeWriter.class.getDeclaredField("stack");
        stackFieldOverride.setAccessible(true);
        stackFieldOverride.set(writerOverride, stack);

        Field pendingNameFieldOverride = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameFieldOverride.setAccessible(true);
        pendingNameFieldOverride.set(writerOverride, name);

        // Set serializeNulls field in the superclass JsonWriter to true via reflection
        // (serializeNulls is a private boolean field in JsonWriter)
        Class<?> jsonWriterClass = JsonTreeWriter.class.getSuperclass();
        Field serializeNullsField = jsonWriterClass.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        serializeNullsField.setBoolean(writerOverride, true);

        Method putMethodOverride = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethodOverride.setAccessible(true);

        // Act
        putMethodOverride.invoke(writerOverride, value);

        // Assert
        verify(jsonObject).add(name, value);
        assertNull(pendingNameFieldOverride.get(writerOverride));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNotNull_valueIsJsonNull_serializeNullsFalse_doesNotAdd() throws Exception {
        // Arrange
        String name = "key";
        pendingNameField.set(jsonTreeWriter, name);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        JsonObject jsonObject = spy(new JsonObject());
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonObject);

        // Explicitly set serializeNulls to false via reflection to ensure correct behavior
        Class<?> jsonWriterClass = JsonTreeWriter.class.getSuperclass();
        Field serializeNullsField = jsonWriterClass.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        serializeNullsField.setBoolean(jsonTreeWriter, false);

        // Act
        invokePut(value);

        // Assert
        verify(jsonObject, never()).add(anyString(), any());
        assertNull(pendingNameField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withEmptyStack_setsProduct() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        JsonElement value = mock(JsonElement.class);

        // Act
        invokePut(value);

        // Assert
        assertSame(value, productField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withStackContainingJsonArray_addsValueToArray() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);

        JsonArray jsonArray = spy(new JsonArray());
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonArray);

        JsonElement value = mock(JsonElement.class);

        // Act
        invokePut(value);

        // Assert
        verify(jsonArray).add(value);
    }

    @Test
    @Timeout(8000)
    public void testPut_withStackContainingNonArrayNonPendingName_throwsIllegalStateException() throws Exception {
        // Arrange
        pendingNameField.set(jsonTreeWriter, null);

        // Use real JsonObject (not mock) because instanceof checks can't be mocked
        JsonObject jsonObject = new JsonObject();

        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonObject);

        JsonElement value = mock(JsonElement.class);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                putMethod.invoke(jsonTreeWriter, value);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof IllegalStateException) {
                    throw (IllegalStateException) e.getCause();
                }
                throw e;
            }
        });
        assertNotNull(thrown);
    }
}