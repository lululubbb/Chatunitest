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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonArray_662_6Test {

    private JsonArray jsonArray;
    private JsonElement mockSingleElement;

    @BeforeEach
    void setUp() {
        jsonArray = new JsonArray();
        mockSingleElement = mock(JsonElement.class);
    }

    @Test
    @Timeout(8000)
    void getAsByte_singleElement_returnsByte() {
        // Arrange
        jsonArray.add(mockSingleElement);
        when(mockSingleElement.getAsByte()).thenReturn((byte) 123);

        // Act
        byte result = jsonArray.getAsByte();

        // Assert
        assertEquals((byte) 123, result);
        verify(mockSingleElement).getAsByte();
    }

    @Test
    @Timeout(8000)
    void getAsByte_emptyArray_throwsException() throws Exception {
        // Use reflection to invoke private getAsSingleElement which should throw if empty
        Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElement.invoke(jsonArray);
        });
        // The cause should be IllegalStateException or IndexOutOfBoundsException depending on implementation
        assertTrue(exception.getCause() instanceof IllegalStateException 
                || exception.getCause() instanceof IndexOutOfBoundsException);
    }

    @Test
    @Timeout(8000)
    void getAsSingleElement_multipleElements_throwsException() throws Exception {
        // Add multiple elements
        jsonArray.add(mockSingleElement);
        jsonArray.add(mockSingleElement);

        Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            getAsSingleElement.invoke(jsonArray);
        });
        // The cause should be IllegalStateException because multiple elements exist
        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    void getAsSingleElement_singleElement_returnsElement() throws Exception {
        jsonArray.add(mockSingleElement);

        Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        Object result = getAsSingleElement.invoke(jsonArray);
        assertSame(mockSingleElement, result);
    }
}