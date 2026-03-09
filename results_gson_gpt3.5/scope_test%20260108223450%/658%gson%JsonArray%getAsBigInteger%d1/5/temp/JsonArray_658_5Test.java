package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
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
import java.math.BigInteger;

public class JsonArray_658_5Test {

    private JsonArray jsonArray;
    private JsonElement mockElement;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
        mockElement = mock(JsonElement.class);
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigInteger_whenSingleElement_returnsBigInteger() {
        jsonArray.add(mockElement);
        BigInteger expected = BigInteger.valueOf(12345L);
        when(mockElement.getAsBigInteger()).thenReturn(expected);

        BigInteger actual = jsonArray.getAsBigInteger();

        assertEquals(expected, actual);
        verify(mockElement).getAsBigInteger();
    }

    @Test
    @Timeout(8000)
    public void testGetAsBigInteger_whenEmpty_throwsException() throws Exception {
        // Use reflection to invoke private getAsSingleElement to simulate empty list scenario
        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);

        // Expect InvocationTargetException caused by IllegalStateException when no elements present
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElementMethod.invoke(jsonArray);
        });

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalStateException);
        assertEquals("Array must have size 1, but has size 0", exception.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGetAsSingleElement_reflection_access() throws Exception {
        jsonArray.add(mockElement);

        Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElementMethod.setAccessible(true);

        JsonElement element = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

        assertSame(mockElement, element);
    }
}