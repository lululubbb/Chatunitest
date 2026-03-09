package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public class JsonIOException_62_5Test {

    @Test
    @Timeout(8000)
    public void testConstructor_String_Throwable() {
        String message = "error message";
        Throwable cause = new RuntimeException("cause");
        JsonIOException exception = new JsonIOException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_String() {
        String message = "error message only";
        JsonIOException exception = new JsonIOException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_Throwable() {
        Throwable cause = new IllegalArgumentException("illegal arg");
        JsonIOException exception = new JsonIOException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
        var field = JsonIOException.class.getDeclaredField("serialVersionUID");
        assertTrue(Modifier.isStatic(field.getModifiers()));
        assertTrue(Modifier.isFinal(field.getModifiers()));
        field.setAccessible(true);
        long value = field.getLong(null);
        assertEquals(1L, value);
    }

    @Test
    @Timeout(8000)
    public void testPrivateConstructorUsingReflection() throws Exception {
        Constructor<JsonIOException> ctor = JsonIOException.class.getDeclaredConstructor(String.class, Throwable.class);
        ctor.setAccessible(true);
        String msg = "reflective message";
        Throwable cause = new Exception("reflective cause");
        JsonIOException ex = ctor.newInstance(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}