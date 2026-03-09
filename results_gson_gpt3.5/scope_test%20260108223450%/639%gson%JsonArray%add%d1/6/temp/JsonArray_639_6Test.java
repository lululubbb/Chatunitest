package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_639_6Test {

    private JsonArray jsonArray;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    public void testAdd_NullBoolean_AddsJsonNullInstance() throws Exception {
        jsonArray.add((Boolean) null);

        // Access private field elements via reflection
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        assertEquals(1, elements.size());
        assertSame(JsonNull.INSTANCE, elements.get(0));
    }

    @Test
    @Timeout(8000)
    public void testAdd_TrueBoolean_AddsJsonPrimitive() throws Exception {
        jsonArray.add(Boolean.TRUE);

        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        assertEquals(1, elements.size());
        JsonElement element = elements.get(0);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(Boolean.TRUE, ((JsonPrimitive) element).getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testAdd_FalseBoolean_AddsJsonPrimitive() throws Exception {
        jsonArray.add(Boolean.FALSE);

        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        assertEquals(1, elements.size());
        JsonElement element = elements.get(0);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(Boolean.FALSE, ((JsonPrimitive) element).getAsBoolean());
    }
}