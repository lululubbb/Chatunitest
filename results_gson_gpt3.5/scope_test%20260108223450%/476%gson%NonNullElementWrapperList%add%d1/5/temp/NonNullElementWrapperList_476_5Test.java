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

class NonNullElementWrapperList_476_5Test {

    private NonNullElementWrapperList<String> list;
    private ArrayList<String> delegate;

    @BeforeEach
    void setUp() {
        delegate = spy(new ArrayList<>());
        list = new NonNullElementWrapperList<>(delegate);
    }

    @Test
    @Timeout(8000)
    void testAdd_validElement_callsDelegateAdd() {
        list.add(0, "element");
        verify(delegate).add(0, "element");
        assertEquals(1, list.size());
        assertEquals("element", list.get(0));
    }

    @Test
    @Timeout(8000)
    void testAdd_nullElement_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> list.add(0, null));
        assertNotNull(thrown);
        verify(delegate, never()).add(anyInt(), any());
    }

    @Test
    @Timeout(8000)
    void testNonNull_privateMethod_withNonNullElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);
        String input = "test";
        Object result = nonNullMethod.invoke(list, input);
        assertEquals(input, result);
    }

    @Test
    @Timeout(8000)
    void testNonNull_privateMethod_withNullElement_throwsException() throws NoSuchMethodException {
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> {
            try {
                nonNullMethod.invoke(list, new Object[] { null });
            } catch (InvocationTargetException e) {
                // Expect NullPointerException as cause
                assertTrue(e.getCause() instanceof NullPointerException);
                throw e;
            }
        });
    }
}