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

class NonNullElementWrapperList_476_6Test {

    private NonNullElementWrapperList<String> list;
    private ArrayList<String> delegate;

    @BeforeEach
    void setUp() {
        delegate = spy(new ArrayList<>());
        list = new NonNullElementWrapperList<>(delegate);
    }

    @Test
    @Timeout(8000)
    void add_validElement_addsElementAtIndex() {
        list.add(0, "test");
        verify(delegate).add(0, "test");
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test
    @Timeout(8000)
    void add_nullElement_throwsNullPointerException() throws Exception {
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            nonNullMethod.invoke(list, (Object) null);
        });
        assertNotNull(thrown);
        assertTrue(thrown.getCause() instanceof NullPointerException);
    }

    @Test
    @Timeout(8000)
    void nonNull_withNonNullElement_returnsSameElement() throws Exception {
        Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
        nonNullMethod.setAccessible(true);

        String element = "element";
        Object result = nonNullMethod.invoke(list, element);

        assertSame(element, result);
    }

    @Test
    @Timeout(8000)
    void add_withIndexOutOfBounds_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "element"));
    }
}