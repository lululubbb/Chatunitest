package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class JsonPrimitive_523_3Test {

    @Test
    @Timeout(8000)
    public void testConstructorWithBoolean_Null_ThrowsNullPointerException() {
        Constructor<JsonPrimitive> ctor;
        try {
            ctor = JsonPrimitive.class.getConstructor(Boolean.class);
        } catch (NoSuchMethodException e) {
            fail("Constructor with Boolean parameter not found");
            return;
        }

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            try {
                ctor.newInstance((Boolean) null);
            } catch (ReflectiveOperationException e) {
                // unwrap InvocationTargetException to throw cause
                Throwable cause = e.getCause();
                if (cause != null) {
                    if (cause instanceof NullPointerException) {
                        throw (NullPointerException) cause;
                    } else {
                        throw new RuntimeException(cause);
                    }
                } else {
                    throw new RuntimeException(e);
                }
            }
        });
        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithBoolean_ValueIsSet() throws Exception {
        Boolean bool = Boolean.TRUE;
        Constructor<JsonPrimitive> ctor = JsonPrimitive.class.getConstructor(Boolean.class);
        JsonPrimitive jsonPrimitive = ctor.newInstance(bool);

        Field valueField = JsonPrimitive.class.getDeclaredField("value");
        valueField.setAccessible(true);
        Object value = valueField.get(jsonPrimitive);

        assertSame(bool, value);
        assertTrue(jsonPrimitive.isBoolean());
        assertEquals(bool.booleanValue(), jsonPrimitive.getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testIsBooleanAndGetAsBoolean() throws Exception {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.FALSE);
        assertTrue(jsonPrimitive.isBoolean());
        assertFalse(jsonPrimitive.getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        JsonPrimitive jp1 = new JsonPrimitive(Boolean.TRUE);
        JsonPrimitive jp2 = new JsonPrimitive(Boolean.TRUE);
        JsonPrimitive jp3 = new JsonPrimitive(Boolean.FALSE);

        assertEquals(jp1, jp2);
        assertEquals(jp1.hashCode(), jp2.hashCode());
        assertNotEquals(jp1, jp3);
        assertNotEquals(jp1.hashCode(), jp3.hashCode());
        assertNotEquals(jp1, null);
        assertNotEquals(jp1, new Object());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsIntegralMethod() throws Exception {
        Constructor<JsonPrimitive> ctor = JsonPrimitive.class.getConstructor(Boolean.class);
        JsonPrimitive jsonPrimitive = ctor.newInstance(Boolean.TRUE);

        java.lang.reflect.Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
        isIntegral.setAccessible(true);

        Boolean result = (Boolean) isIntegral.invoke(null, jsonPrimitive);
        assertFalse(result);

        // Create a JsonPrimitive with an integral Number to test true case
        Constructor<JsonPrimitive> numCtor = JsonPrimitive.class.getConstructor(Number.class);
        JsonPrimitive integralPrimitive = numCtor.newInstance(123);

        Boolean integralResult = (Boolean) isIntegral.invoke(null, integralPrimitive);
        assertTrue(integralResult);
    }
}