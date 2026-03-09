package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

class JsonArrayRemoveTest {

    private JsonArray jsonArray;
    private JsonElement element1;
    private JsonElement element2;

    @BeforeEach
    public void setUp() throws Exception {
        jsonArray = new JsonArray();

        // Create mock JsonElement instances
        element1 = mock(JsonElement.class);
        element2 = mock(JsonElement.class);

        // Use reflection to access the private final 'elements' field and initialize it with an ArrayList containing element1 and element2
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);

        ArrayList<JsonElement> elementsList = new ArrayList<>();
        elementsList.add(element1);
        elementsList.add(element2);
        elementsField.set(jsonArray, elementsList);
    }

    @Test
    @Timeout(8000)
    public void testRemove_ElementPresent() {
        // element1 is present in elements list, removal should return true
        boolean removed = jsonArray.remove(element1);
        assertTrue(removed);

        // Verify that element1 is no longer in the list
        try {
            Field elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
            assertFalse(elements.contains(element1));
            assertEquals(1, elements.size());
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testRemove_ElementNotPresent() {
        // Create a new mock element not in the list
        JsonElement notPresent = mock(JsonElement.class);

        boolean removed = jsonArray.remove(notPresent);
        assertFalse(removed);

        // Verify that the original elements remain unchanged
        try {
            Field elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
            assertEquals(2, elements.size());
            assertTrue(elements.contains(element1));
            assertTrue(elements.contains(element2));
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testRemove_NullElement() {
        // Removing null element should behave as per ArrayList.remove(null)
        // Since elements list does not contain null, removal should return false
        boolean removed = jsonArray.remove(null);
        assertFalse(removed);

        // Verify list unchanged
        try {
            Field elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
            assertEquals(2, elements.size());
            assertTrue(elements.contains(element1));
            assertTrue(elements.contains(element2));
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}