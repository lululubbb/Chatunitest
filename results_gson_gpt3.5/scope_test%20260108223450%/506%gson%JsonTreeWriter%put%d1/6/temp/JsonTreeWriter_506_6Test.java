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
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_506_6Test {

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
    public void testPut_withPendingNameNotNull_andValueNotJsonNull_andSerializeNullsFalse_addsToJsonObject() throws Exception {
        // Setup pendingName
        pendingNameField.set(jsonTreeWriter, "name");

        // Setup stack with a JsonObject on top
        List<JsonElement> stack = new ArrayList<>();
        JsonObject jsonObject = spy(new JsonObject());
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        // Mock value.isJsonNull() to false
        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(false);

        // Mock peek() to return jsonObject
        // peek() is private, but it returns stack.get(stack.size() - 1)
        // We have set stack with jsonObject on top, so peek() returns jsonObject automatically.

        // Mock getSerializeNulls() to false
        try (MockedStatic<JsonTreeWriter> ignored = mockStatic(JsonTreeWriter.class)) {
            // getSerializeNulls() is instance method from JsonWriter, which JsonTreeWriter extends.
            // We cannot mock instance method with static mock, so we will spy the instance instead.
        }
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        doReturn(false).when(spyWriter).getSerializeNulls();

        // invoke put on spyWriter
        putMethod.invoke(spyWriter, value);

        // Verify jsonObject.add called with pendingName and value
        verify(jsonObject).add("name", value);

        // Verify pendingName is set to null
        assertNull(pendingNameField.get(spyWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNotNull_andValueIsJsonNull_andSerializeNullsTrue_addsToJsonObject() throws Exception {
        pendingNameField.set(jsonTreeWriter, "name");

        List<JsonElement> stack = new ArrayList<>();
        JsonObject jsonObject = spy(new JsonObject());
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        doReturn(true).when(spyWriter).getSerializeNulls();

        putMethod.invoke(spyWriter, value);

        verify(jsonObject).add("name", value);
        assertNull(pendingNameField.get(spyWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNotNull_andValueIsJsonNull_andSerializeNullsFalse_doesNotAdd() throws Exception {
        pendingNameField.set(jsonTreeWriter, "name");

        List<JsonElement> stack = new ArrayList<>();
        JsonObject jsonObject = spy(new JsonObject());
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);
        when(value.isJsonNull()).thenReturn(true);

        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        doReturn(false).when(spyWriter).getSerializeNulls();

        putMethod.invoke(spyWriter, value);

        verify(jsonObject, never()).add(anyString(), any());
        assertNull(pendingNameField.get(spyWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNull_andStackEmpty_setsProduct() throws Exception {
        pendingNameField.set(jsonTreeWriter, null);

        List<JsonElement> stack = new ArrayList<>();
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        putMethod.invoke(jsonTreeWriter, value);

        assertSame(value, productField.get(jsonTreeWriter));
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNull_andStackTopJsonArray_addsToJsonArray() throws Exception {
        pendingNameField.set(jsonTreeWriter, null);

        List<JsonElement> stack = new ArrayList<>();
        JsonArray jsonArray = spy(new JsonArray());
        stack.add(jsonArray);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        putMethod.invoke(jsonTreeWriter, value);

        verify(jsonArray).add(value);
    }

    @Test
    @Timeout(8000)
    public void testPut_withPendingNameNull_andStackTopNotJsonArray_throwsIllegalStateException() throws Exception {
        pendingNameField.set(jsonTreeWriter, null);

        List<JsonElement> stack = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        stack.add(jsonObject);
        stackField.set(jsonTreeWriter, stack);

        JsonElement value = mock(JsonElement.class);

        Exception exception = assertThrows(Exception.class, () -> putMethod.invoke(jsonTreeWriter, value));
        // putMethod.invoke wraps exceptions in InvocationTargetException
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
    }
}