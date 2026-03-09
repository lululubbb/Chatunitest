package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_657_4Test {

    private JsonArray jsonArray;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigDecimal_singleElement() {
        // Prepare a mock JsonElement to return a specific BigDecimal
        JsonElement mockElement = mock(JsonElement.class);
        BigDecimal expected = new BigDecimal("123.45");
        when(mockElement.getAsBigDecimal()).thenReturn(expected);

        // Use reflection to set the private 'elements' field with one mock element
        try {
            java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.ArrayList<JsonElement> elements = new java.util.ArrayList<>();
            elements.add(mockElement);
            elementsField.set(jsonArray, elements);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up elements field via reflection: " + e.getMessage());
        }

        // Call getAsBigDecimal and verify it returns the expected value
        BigDecimal actual = jsonArray.getAsBigDecimal();
        assertEquals(expected, actual);
        verify(mockElement).getAsBigDecimal();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigDecimal_emptyArray_throws() {
        // The elements list is empty by default, getAsSingleElement() should throw
        assertThrows(IllegalStateException.class, () -> {
            jsonArray.getAsBigDecimal();
        });
    }

    @Test
    @Timeout(8000)
    public void testGetAsSingleElement_reflection_withOneElement() throws Exception {
        // Setup elements with one mock element
        JsonElement mockElement = mock(JsonElement.class);
        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.ArrayList<JsonElement> elements = new java.util.ArrayList<>();
        elements.add(mockElement);
        elementsField.set(jsonArray, elements);

        // Access private method getAsSingleElement via reflection
        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);

        JsonElement returned = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);
        assertSame(mockElement, returned);
    }

    @Test
    @Timeout(8000)
    public void testGetAsSingleElement_reflection_empty_throws() throws Exception {
        // Ensure elements is empty
        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.ArrayList<JsonElement> elements = new java.util.ArrayList<>();
        elementsField.set(jsonArray, elements);

        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElementMethod.invoke(jsonArray);
        });
        // The cause should be IllegalStateException or similar
        assertTrue(thrown.getCause() instanceof IllegalStateException);
    }
}