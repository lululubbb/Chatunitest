package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_475_3Test {

    private NonNullElementWrapperList<String> list;
    private ArrayList<String> delegate;

    @BeforeEach
    void setUp() {
        delegate = Mockito.spy(new ArrayList<>());
        list = new NonNullElementWrapperList<>(delegate);
    }

    @Test
    @Timeout(8000)
    void testSet_ReplacesElementSuccessfully() {
        delegate.add("old");
        // index 0 exists now
        String previous = list.set(0, "new");
        assertEquals("old", previous);
        assertEquals("new", delegate.get(0));
        verify(delegate).set(0, "new");
    }

    @Test
    @Timeout(8000)
    void testSet_NullElement_ThrowsNullPointerException() throws Exception {
        delegate.add("old");
        // Use reflection to access private nonNull method to confirm behavior
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> {
            try {
                nonNullMethod.invoke(list, (Object) null);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof NullPointerException) {
                    throw e;
                }
                fail("Expected NullPointerException");
            }
        });

        // The set method should throw NullPointerException if element is null
        assertThrows(NullPointerException.class, () -> list.set(0, null));
    }

    @Test
    @Timeout(8000)
    void testSet_IndexOutOfBounds_ThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "new"));
    }

    @Test
    @Timeout(8000)
    void testNonNullMethod_WithNonNullElement_ReturnsElement() throws Exception {
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);
        String input = "test";
        String result = (String) nonNullMethod.invoke(list, input);
        assertSame(input, result);
    }
}