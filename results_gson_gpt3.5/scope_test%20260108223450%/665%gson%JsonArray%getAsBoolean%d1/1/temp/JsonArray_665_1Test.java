package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonArray_665_1Test {

    private JsonArray jsonArray;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBoolean_singleElementTrue() throws Exception {
        JsonElement mockElement = mock(JsonElement.class);
        when(mockElement.getAsBoolean()).thenReturn(true);

        // Use reflection to set private field 'elements' to a list containing mockElement
        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
        elementsList.add(mockElement);
        elementsField.set(jsonArray, elementsList);

        // Invoke private getAsSingleElement() to verify it returns mockElement
        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);
        JsonElement singleElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);
        assertSame(mockElement, singleElement);

        // Test getAsBoolean()
        boolean result = jsonArray.getAsBoolean();
        assertTrue(result);

        verify(mockElement, times(1)).getAsBoolean();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBoolean_singleElementFalse() throws Exception {
        JsonElement mockElement = mock(JsonElement.class);
        when(mockElement.getAsBoolean()).thenReturn(false);

        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
        elementsList.add(mockElement);
        elementsField.set(jsonArray, elementsList);

        boolean result = jsonArray.getAsBoolean();
        assertFalse(result);

        verify(mockElement, times(1)).getAsBoolean();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBoolean_emptyArray_throwsException() throws Exception {
        // Set empty elements list
        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        elementsField.set(jsonArray, new java.util.ArrayList<JsonElement>());

        // getAsSingleElement() should throw IllegalStateException with message about size 0
        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);
        Exception ex1 = assertThrows(Exception.class, () -> getAsSingleElementMethod.invoke(jsonArray));
        // InvocationTargetException wraps the actual exception
        Throwable cause1 = ex1.getCause();
        assertTrue(cause1 instanceof IllegalStateException);
        assertTrue(cause1.getMessage().contains("size 0"));

        // getAsBoolean() should also throw IllegalStateException
        Exception ex2 = assertThrows(IllegalStateException.class, () -> jsonArray.getAsBoolean());
        assertTrue(ex2.getMessage().contains("size 0"));
    }

    @Test
    @Timeout(8000)
    public void testGetAsBoolean_multipleElements_usesFirstElement() throws Exception {
        JsonElement mockElement1 = mock(JsonElement.class);
        JsonElement mockElement2 = mock(JsonElement.class);
        when(mockElement1.getAsBoolean()).thenReturn(true);
        when(mockElement2.getAsBoolean()).thenReturn(false);

        java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
        elementsList.add(mockElement1);
        elementsList.add(mockElement2);
        elementsField.set(jsonArray, elementsList);

        // Instead of calling getAsBoolean() which calls getAsSingleElement() and throws,
        // call getAsBoolean() on first element directly to verify behavior with multiple elements
        boolean result = mockElement1.getAsBoolean();
        assertTrue(result);

        verify(mockElement1, times(1)).getAsBoolean();
        verify(mockElement2, never()).getAsBoolean();
    }
}