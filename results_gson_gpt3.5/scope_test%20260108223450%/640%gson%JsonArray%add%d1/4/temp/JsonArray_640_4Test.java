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

class JsonArray_640_4Test {

    private JsonArray jsonArray;

    @BeforeEach
    void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    void testAddWithNonNullCharacter() throws Exception {
        Character ch = 'a';

        jsonArray.add(ch);

        // Access private field elements via reflection
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        assertEquals(1, elements.size());
        JsonElement element = elements.get(0);
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(ch, primitive.getAsCharacter());
    }

    @Test
    @Timeout(8000)
    void testAddWithNullCharacter() throws Exception {
        Character ch = null;

        jsonArray.add(ch);

        // Access private field elements via reflection
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        assertEquals(1, elements.size());
        JsonElement element = elements.get(0);
        assertSame(JsonNull.INSTANCE, element);
    }
}