package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class JsonArray_657_3Test {

    private JsonArray jsonArray;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigDecimal_singleElementDelegates() {
        // Arrange
        JsonElement mockElement = mock(JsonElement.class);
        BigDecimal expected = new BigDecimal("123.45");
        when(mockElement.getAsBigDecimal()).thenReturn(expected);

        // Use reflection to set private elements list with one mockElement
        try {
            var elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            var list = new java.util.ArrayList<JsonElement>();
            list.add(mockElement);
            elementsField.set(jsonArray, list);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to set elements field: " + e);
        }

        // Act
        BigDecimal actual = jsonArray.getAsBigDecimal();

        // Assert
        assertEquals(expected, actual);
        verify(mockElement).getAsBigDecimal();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigDecimal_emptyArray_throwsException() throws Exception {
        // Arrange
        // elements list is empty by default (new JsonArray())

        // Use reflection to invoke private getAsSingleElement to verify exception
        Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        // Act & Assert
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElement.invoke(jsonArray);
        });
        assertNotNull(thrown.getCause());
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigDecimal_multipleElements_throwsException() throws Exception {
        // Arrange
        JsonElement mockElement1 = mock(JsonElement.class);
        JsonElement mockElement2 = mock(JsonElement.class);

        try {
            var elementsField = JsonArray.class.getDeclaredField("elements");
            elementsField.setAccessible(true);
            var list = new java.util.ArrayList<JsonElement>();
            list.add(mockElement1);
            list.add(mockElement2);
            elementsField.set(jsonArray, list);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to set elements field: " + e);
        }

        // Use reflection to invoke private getAsSingleElement to verify exception
        Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        // Act & Assert
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElement.invoke(jsonArray);
        });
        assertNotNull(thrown.getCause());
    }
}