package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class Primitives_264_2Test {

    @Test
    @Timeout(8000)
    void testIsPrimitive_withPrimitiveTypes() {
        assertTrue(Primitives.isPrimitive(boolean.class));
        assertTrue(Primitives.isPrimitive(byte.class));
        assertTrue(Primitives.isPrimitive(char.class));
        assertTrue(Primitives.isPrimitive(short.class));
        assertTrue(Primitives.isPrimitive(int.class));
        assertTrue(Primitives.isPrimitive(long.class));
        assertTrue(Primitives.isPrimitive(float.class));
        assertTrue(Primitives.isPrimitive(double.class));
        assertTrue(Primitives.isPrimitive(void.class));
    }

    @Test
    @Timeout(8000)
    void testIsPrimitive_withNonPrimitiveTypes() {
        assertFalse(Primitives.isPrimitive(Boolean.class));
        assertFalse(Primitives.isPrimitive(String.class));
        assertFalse(Primitives.isPrimitive(Object.class));
        assertFalse(Primitives.isPrimitive(null));
    }

    @Test
    @Timeout(8000)
    void testIsWrapperType_withWrapperTypes() {
        assertTrue(Primitives.isWrapperType(Boolean.class));
        assertTrue(Primitives.isWrapperType(Byte.class));
        assertTrue(Primitives.isWrapperType(Character.class));
        assertTrue(Primitives.isWrapperType(Short.class));
        assertTrue(Primitives.isWrapperType(Integer.class));
        assertTrue(Primitives.isWrapperType(Long.class));
        assertTrue(Primitives.isWrapperType(Float.class));
        assertTrue(Primitives.isWrapperType(Double.class));
        assertTrue(Primitives.isWrapperType(Void.class));
    }

    @Test
    @Timeout(8000)
    void testIsWrapperType_withNonWrapperTypes() {
        assertFalse(Primitives.isWrapperType(boolean.class));
        assertFalse(Primitives.isWrapperType(String.class));
        assertFalse(Primitives.isWrapperType(Object.class));
        assertFalse(Primitives.isWrapperType(null));
    }

    @Test
    @Timeout(8000)
    void testWrap_withPrimitiveTypes() {
        assertEquals(Boolean.class, Primitives.wrap(boolean.class));
        assertEquals(Byte.class, Primitives.wrap(byte.class));
        assertEquals(Character.class, Primitives.wrap(char.class));
        assertEquals(Short.class, Primitives.wrap(short.class));
        assertEquals(Integer.class, Primitives.wrap(int.class));
        assertEquals(Long.class, Primitives.wrap(long.class));
        assertEquals(Float.class, Primitives.wrap(float.class));
        assertEquals(Double.class, Primitives.wrap(double.class));
        assertEquals(Void.class, Primitives.wrap(void.class));
    }

    @Test
    @Timeout(8000)
    void testWrap_withNonPrimitiveTypes() {
        assertEquals(String.class, Primitives.wrap(String.class));
        assertEquals(Object.class, Primitives.wrap(Object.class));
        assertNull(Primitives.wrap(null));
    }

    @Test
    @Timeout(8000)
    void testUnwrap_withWrapperTypes() {
        assertEquals(boolean.class, Primitives.unwrap(Boolean.class));
        assertEquals(byte.class, Primitives.unwrap(Byte.class));
        assertEquals(char.class, Primitives.unwrap(Character.class));
        assertEquals(short.class, Primitives.unwrap(Short.class));
        assertEquals(int.class, Primitives.unwrap(Integer.class));
        assertEquals(long.class, Primitives.unwrap(Long.class));
        assertEquals(float.class, Primitives.unwrap(Float.class));
        assertEquals(double.class, Primitives.unwrap(Double.class));
        assertEquals(void.class, Primitives.unwrap(Void.class));
    }

    @Test
    @Timeout(8000)
    void testUnwrap_withNonWrapperTypes() {
        assertEquals(String.class, Primitives.unwrap(String.class));
        assertEquals(Object.class, Primitives.unwrap(Object.class));
        assertNull(Primitives.unwrap(null));
    }
}