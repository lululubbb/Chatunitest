package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_512_2Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_withNonNullString_returnsThisAndAddsJsonPrimitive() throws Exception {
        String testString = "test";

        JsonWriter result = jsonTreeWriter.value(testString);

        assertSame(jsonTreeWriter, result);

        // Use reflection to access private field 'stack' and verify it contains JsonPrimitive with value "test"
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        // The stack may contain a JsonObject or JsonArray at the top level, so get product field instead
        var productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

        assertTrue(product instanceof JsonPrimitive);
        assertEquals(testString, ((JsonPrimitive) product).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testValue_withNullString_callsNullValue() throws IOException {
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);

        doReturn(spyWriter).when(spyWriter).nullValue();

        // Cast null to String to disambiguate the overloaded value method
        JsonWriter result = spyWriter.value((String) null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, result);
    }

    @Test
    @Timeout(8000)
    public void testPutMethodAddsElementToStack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Access private method put(JsonElement)
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        JsonPrimitive jsonPrimitive = new JsonPrimitive("hello");

        putMethod.invoke(jsonTreeWriter, jsonPrimitive);

        var productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

        assertEquals(jsonPrimitive, product);
    }
}